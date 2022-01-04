import "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"

$('form').submit(e => {
    e.preventDefault();
    console.log($('#password').val());
    if ($('#password').val() !== $('#conf-password').val()) {
        alert('passwords do not match!');
        return;
    }
    const o = {
        email: $('#email').val(),
        username: $('#username').val(),
        password: $('#password').val(),
    };
    fetch('/api/register', {
        method: 'POST',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(o)
    }).then(response => response.json()).then(data => {
        if (data.status === 500) {
            alert('invalid register data!');
            return;
        }
        window.location.assign('/login');
    }).catch(err => {
        console.error(err);
        alert('smth went wrong on server!');
    })
})