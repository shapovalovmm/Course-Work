
// Инициализация страницы
document.addEventListener('DOMContentLoaded', function() {
    const taleId = getTaleId();

    // Загружаем содержимое сказки
    loadTaleContent(taleId);

    // Инициализируем обработчики событий для кнопок
    setupInteractionFunctionality();
});

// Настройка обработчиков событий
function setupInteractionFunctionality() {
    const likeButton = document.getElementById('likeButton');
    const favoriteButton = document.getElementById('favoriteButton');
    const taleId = getTaleId();

    // Проверяем предыдущие действия пользователя
    checkUserInteractions(taleId);

    // Обработчик клика по кнопке лайка
    likeButton.addEventListener('click', function() {
        // Проверяем авторизацию
        if (!isUserAuthenticated()) {
            alert('Будь ласка, увійдіть, щоб оцінити казку');
            return;
        }

        // Отправляем запрос на переключение лайка
        toggleLike(taleId);

        // Добавляем анимацию
        showLikeAnimation(likeButton);
    });

    // Обработчик клика по кнопке избранного
    favoriteButton.addEventListener('click', function () {
        if (!isUserAuthenticated()) {
            alert('Будь ласка, увійдіть, щоб додати казку до вибраного');
            return;
        }

        const isFavorited = favoriteButton.classList.contains('active');

        // UI обновляем сразу (опционально можно обновлять после ответа от сервера)
        if (isFavorited) {
            favoriteButton.classList.remove('active');
            favoriteButton.innerHTML = '<span class="icon">⭐</span> У вибране';
        } else {
            favoriteButton.classList.add('active');
            favoriteButton.innerHTML = '<span class="icon">⭐</span> У вибраному';
        }

        // Отправляем запрос на сервер
        toggleFavorite(taleId);

        // Обновляем localStorage
        saveUserInteraction(taleId, 'favorite', !isFavorited);
    });
}

// Функция для показа анимации при лайке
function showLikeAnimation(likeButton) {
    const heart = document.createElement('span');
    heart.className = 'floating-heart';
    heart.textContent = '❤️';
    heart.style.cssText = `
        position: absolute;
        font-size: 24px;
        animation: float-up 1s ease-out forwards;
        opacity: 0;
        left: ${likeButton.offsetLeft + likeButton.offsetWidth / 2}px;
        top: ${likeButton.offsetTop}px;
    `;
    document.body.appendChild(heart);

    // Удаляем элемент после завершения анимации
    setTimeout(() => heart.remove(), 1000);
}

// Функция для проверки, авторизован ли пользователь
function isUserAuthenticated() {
    const user = JSON.parse(localStorage.getItem('user'));
    return user && user.token;
}

// Функция для получения ID сказки из URL
function getTaleId() {
    const urlParams = new URLSearchParams(window.location.search);
    return urlParams.get('id') || '1';
}

// Функция для проверки предыдущих действий пользователя
function checkUserInteractions(taleId) {
    // Проверяем локальные предпочтения (избранное)
    const userInteractions = getUserInteractions();
    const favoriteButton = document.getElementById('favoriteButton');

    // Проверяем, добавлена ли сказка в избранное
    if (userInteractions.favorites && userInteractions.favorites.includes(taleId)) {
        favoriteButton.classList.add('active');
        favoriteButton.innerHTML = '<span class="icon">⭐</span> У вибраному';
    }
    checkFavoriteStatus(taleId);
    // Проверяем статус лайка с сервера и количество лайков
    checkLikeStatus(taleId);
}

// Функция для проверки статуса лайка на сервере
function checkLikeStatus(taleId) {
    const user = JSON.parse(localStorage.getItem('user'));
    const likeButton = document.getElementById('likeButton');

    if (!user || !user.token) {
        // Если пользователь не авторизован, просто получаем количество лайков
        fetchLikeCount(taleId);
        return;
    }

    // Делаем запрос к API для проверки, поставил ли пользователь лайк
    fetch(`http://localhost:8080/api/tales/${taleId}/like/status`, {
        headers: {
            'Authorization': 'Bearer ' + user.token
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to check like status');
            }
            return response.json();
        })
        .then(data => {
            // Обновляем UI на основе ответа сервера
            if (data.hasLiked) {
                likeButton.classList.add('active');
            } else {
                likeButton.classList.remove('active');
            }

            // Обновляем счетчик лайков
            updateLikeCounterText(data.likes);
        })
        .catch(error => {
            console.error('Error checking like status:', error);
            // Если запрос не удался, делаем запрос только на количество лайков
            fetchLikeCount(taleId);
        });
}

// Функция для загрузки контента сказки
function loadTaleContent(taleId) {
    fetch(`http://localhost:8080/api/tales/${taleId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch tale');
            }
            return response.json();
        })
        .then(tale => {
            // Обновляем заголовок и содержимое
            const titleElement = document.getElementById('taleTitle');
            const authorElement = document.getElementById('taleAuthor');
            const contentElement = document.getElementById('taleContent');

            if (titleElement) titleElement.textContent = tale.title;
            if (authorElement) authorElement.textContent = `Автор: ${tale.author}`;
            if (contentElement) contentElement.textContent = tale.content;

            // Обновляем счетчик лайков
            updateLikeCounterText(tale.likes);
        })
        .catch(error => {
            console.error('Error loading tale:', error);
        });
}

// Функция для отправки запроса на лайк/анлайк
async function toggleLike(taleId) {
    const user = JSON.parse(localStorage.getItem('user'));
    const likeButton = document.getElementById('likeButton');

    if (!user || !user.token) {
        alert('⚠️ Будь ласка, увійдіть в систему, щоб ставити лайки');
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/api/tales/${taleId}/like`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + user.token
            }
        });

        if (!response.ok) {
            throw new Error('Failed to toggle like');
        }

        const data = await response.json();

        // Обновляем UI на основе ответа сервера
        updateLikeCounterText(data.likes);

        // Обновляем состояние кнопки
        if (data.hasLiked) {
            likeButton.classList.add('active');
        } else {
            likeButton.classList.remove('active');
        }

        return data;
    } catch (error) {
        console.error('Error toggling like:', error);
        alert('❌ Не вдалося змінити статус лайка');
    }
}

// Функция для обновления текста счетчика лайков с учетом склонения
function updateLikeCounterText(count) {
    const likeCounter = document.getElementById('likeCounter');

    // Ukrainian language has different plural forms
    let text;
    if (count % 10 === 1 && count % 100 !== 11) {
        text = `${count} уподобання`;
    } else if ([2, 3, 4].includes(count % 10) && ![12, 13, 14].includes(count % 100)) {
        text = `${count} уподобання`;
    } else {
        text = `${count} уподобань`;
    }

    likeCounter.textContent = text;
}

// Функция для получения количества лайков с сервера
function fetchLikeCount(taleId) {
    fetch(`http://localhost:8080/api/tales/${taleId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to fetch tale data');
            }
            return response.json();
        })
        .then(data => {
            if (data && data.likes !== undefined) {
                updateLikeCounterText(data.likes);
            }
        })
        .catch(error => {
            console.error('Error fetching tale data:', error);
        });
}

// Получение пользовательских взаимодействий из localStorage
function getUserInteractions() {
    const interactions = localStorage.getItem('taleInteractions');
    return interactions ? JSON.parse(interactions) : { likes: [], favorites: [] };
}
async function toggleFavorite(taleId) {
    const user = JSON.parse(localStorage.getItem('user'));
    const favoriteButton = document.getElementById('favoriteButton');

    if (!user || !user.token) {
        alert('⚠️ Будь ласка, увійдіть в систему, щоб додати в обране');
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/api/tales/${taleId}/favorite`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + user.token
            }
        });

        if (!response.ok) {
            throw new Error('Не вдалося додати до вибраного');
        }

        // тут можно обработать результат, если сервер что-то возвращает
        // const result = await response.json();

    } catch (error) {
        console.error('Помилка при додаванні в обране:', error);
        alert('❌ Не вдалося змінити статус вибраного');
    }
}
// Сохранение пользовательского взаимодействия в localStorage
function saveUserInteraction(taleId, interactionType, value) {
    const interactions = getUserInteractions();

    if (interactionType === 'favorite') {
        if (value) {
            // Добавляем в избранное, если еще нет
            if (!interactions.favorites.includes(taleId)) {
                interactions.favorites.push(taleId);
            }
        } else {
            // Удаляем из избранного
            interactions.favorites = interactions.favorites.filter(id => id !== taleId);
        }
    }

    // Сохраняем в localStorage
    localStorage.setItem('taleInteractions', JSON.stringify(interactions));
}
function checkFavoriteStatus(taleId) {
    const user = JSON.parse(localStorage.getItem('user'));
    const favoriteButton = document.getElementById('favoriteButton');

    if (!user || !user.token) {
        return; // Не авторизован — ничего не делаем
    }

    fetch(`http://localhost:8080/api/tales/${taleId}/favorite/status`, {
        headers: {
            'Authorization': 'Bearer ' + user.token
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to check favorite status');
            }
            return response.json();
        })
        .then(data => {
            if (data.isFavourite) {
                favoriteButton.classList.add('active');
                favoriteButton.innerHTML = '<span class="icon">⭐</span> У вибраному';
            } else {
                favoriteButton.classList.remove('active');
                favoriteButton.innerHTML = '<span class="icon">⭐</span> У вибране';
            }
        })
        .catch(error => {
            console.error('Error checking favorite status:', error);
        });
}