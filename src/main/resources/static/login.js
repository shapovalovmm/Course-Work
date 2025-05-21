document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('loginForm');

    if (!form) {
        console.error('❌ Не знайдено форму з id="loginForm"');
        return;
    }

    form.addEventListener('submit', async (e) => {
        e.preventDefault();

        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value;

        if (!username || !password) {
            alert('⚠️ Введіть ім\'я користувача та пароль.');
            return;
        }

        try {
            const loginPayload = {
                username: username.trim(),
                password: password
            };

            const response = await fetch('http://127.0.0.1:8080/api/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(loginPayload)
            });


            if (!response.ok) {
                const errorText = await response.text();
                if (response.status === 401) {
                    alert('❌ Невірне ім\'я користувача або пароль.');
                } else {
                    alert(`❌ Помилка сервера (${response.status}): ${errorText}`);
                }
                return;
            }

            const userData = await response.json();

            // Проверка, что userData содержит правильный формат
            if (!userData.token) {
                alert('❌ Отсутствует токен в ответе сервера.');
                return;
            }

            if (!userData.username) {
                alert('❌ Отсутствует имя пользователя в ответе сервера.');
                return;
            }

            if (!userData.roles || !Array.isArray(userData.roles)) {
                alert('❌ Отсутствуют или некорректный формат ролей в ответе сервера.');
                return;
            }

// Сохраняем данные пользователя
            localStorage.setItem('authToken', userData.token);
            localStorage.setItem('user', JSON.stringify({
                username: userData.username,
                roles: userData.roles, // Теперь это массив
                token: userData.token
            }));

            console.log('✅ Успішний вхід:', userData);
            alert('✅ Вхід успішний!');
            window.location.href = 'index.html';

        } catch (error) {
            console.error('❌ Помилка під час входу:', error);
            alert('❌ Не вдалося підключитися до сервера або сталася інша помилка.');
        }

    });
});
