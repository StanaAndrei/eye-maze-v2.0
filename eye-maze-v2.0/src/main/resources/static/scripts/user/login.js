import "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"

$('form').submit(e => {
    e.preventDefault();
    let o = {
        email: $('#email').val(),
        password: $('#password').val()
    };
    fetch('/api/login', {
        method: 'POST',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(o)
    }).then(response => response.json())
        .then(data => {
            if (data === 'WRONG_DATA') {
                alert('wrong login data!');
                return;
            }
            if (data === 'ALREADY_AUTH') {
                alert('you are logged on another device/session');
                return;
            }
            window.location.reload();
        }).catch((error) => {
            alert('smth went wrong on server!');
            console.error('Error:', error);
        });
})