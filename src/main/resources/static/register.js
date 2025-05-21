document.addEventListener('DOMContentLoaded', () => {
    const form = document.getElementById('registerForm');

    form.addEventListener('submit', (e) => {
        e.preventDefault();

        const username = document.getElementById('username').value.trim();
        const password = document.getElementById('password').value;

        const data = {
            username: username,
            password: password
        };

        fetch('http://localhost:8080/api/users', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(data)
        })
            .then(res => {
                if (!res.ok) {
                    throw new Error('Помилка при реєстрації');
                }
                return res.json();
            })
            .then(result => {
                alert('✅ Реєстрація успішна!');
                window.location.href = 'login.html';
            })
            .catch(err => {
                console.error('❌ Помилка POST:', err);
                alert('⚠️ Не вдалося зареєструватися. Спробуйте ще раз.');
            });
    });
});
