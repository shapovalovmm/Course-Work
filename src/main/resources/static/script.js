document.addEventListener('DOMContentLoaded', () => {
    // Функция для загрузки и отображения казок
    loadTales();
    // Функция для приветствия пользователя по имени
    updateWelcomeMessage();
    const createBtn = document.getElementById('createTaleBtn');
    const userData = localStorage.getItem('user');
    if (userData) {
        const user = JSON.parse(userData);
        // Проверяем роли (поддерживается как строка, так и массив)
        if (hasRole(user.roles, 'ROLE_ADMIN')) {
            createBtn.style.display = 'inline-block';
            createBtn.addEventListener('click', () => {
                window.location.href = 'create-tale.html';
            });
        }
    }
});

// Функция для проверки роли (поддерживает как строки, так и массивы)
function hasRole(roles, roleName) {
    if (!roles) return false;

    // Если roles — строка
    if (typeof roles === 'string') {
        return roles === roleName;
    }

    // Если roles — массив
    if (Array.isArray(roles)) {
        return roles.includes(roleName);
    }

    return false;
}

// Функция для отображения приветствия с именем пользователя
function updateWelcomeMessage() {
    const welcomeHeading = document.querySelector('h1');
    if (!welcomeHeading) return;

    // Проверяем, авторизован ли пользователь (данные сохранены в localStorage)
    const userData = localStorage.getItem('user');

    if (userData) {
        try {
            const user = JSON.parse(userData);
            // Проверяем, есть ли поле username
            if (user && user.username) {
                welcomeHeading.textContent = `Вітаємо, ${user.username}!`;
            }
        } catch (error) {
            console.error('Ошибка при обработке данных пользователя:', error);
        }
    }
}

// Функция для загрузки казок с API
async function loadTales() {
    const taleGrid = document.getElementById('taleGrid');
    if (!taleGrid) return;

    try {
        const userData = JSON.parse(localStorage.getItem('user'));
        const token = userData?.token;

        const response = await fetch('http://127.0.0.1:8080/api/tales', {
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error(`Ошибка HTTP: ${response.status}`);
        }

        const tales = await response.json();

        // Очищаем текущее содержимое
        taleGrid.innerHTML = '';

        // Добавляем карточки для каждой казки
        tales.forEach(tale => {
            const taleCard = createTaleCard(tale);
            taleGrid.appendChild(taleCard);
        });

    } catch (error) {
        console.error('Ошибка при загрузке казок:', error);
        taleGrid.innerHTML = '<p class="error">Не вдалося завантажити казки. Будь ласка, спробуйте пізніше.</p>';
    }
}

// Функция для создания карточки казки
function createTaleCard(tale) {
    const card = document.createElement('div');
    card.className = 'item';

    const userData = localStorage.getItem('user');
    let isAdmin = false;

    if (userData) {
        try {
            const user = JSON.parse(userData);
            // Используем функцию hasRole для проверки прав
            isAdmin = hasRole(user.roles, 'ROLE_ADMIN');
        } catch (e) {}
    }

    card.innerHTML = `
        <div class="thumbnail"></div>
        <h2 class="title">${tale.title}</h2>
        <button onclick="viewTale(${tale.id})">Читати</button>
        ${isAdmin ? `
            <button onclick="editTale(${tale.id})">Редагувати</button>
            <button onclick="deleteTale(${tale.id})">Видалити</button>
        ` : ''}
    `;

    return card;
}

function editTale(taleId) {
    window.location.href = `edit-tale.html?id=${taleId}`;
}

async function deleteTale(taleId) {
    const confirmed = confirm('Ви впевнені, що хочете видалити цю казку?');
    if (!confirmed) return;

    const user = JSON.parse(localStorage.getItem('user'));
    const token = user.token;

    try {
        const response = await fetch(`http://127.0.0.1:8080/api/tales/${taleId}`, {
            method: 'DELETE',
            headers: {
                'Authorization': `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error(`Помилка HTTP: ${response.status}`);
        }

        // Перезагрузим список
        loadTales();

    } catch (err) {
        console.error('Помилка при видаленні казки:', err);
        alert('Не вдалося видалити казку');
    }
}

// Функция для просмотра подробностей казки
function viewTale(taleId) {
    window.location.href = `tale.html?id=${taleId}`;
}