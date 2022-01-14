import "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"
let userInfoUpDto = new Object();

function encodeImageFileAsURL(element) {
    var file = element.files[0];
    var reader = new FileReader();
    reader.onloadend = async () => {
        console.log('RESULT', reader.result)
        
    }
    reader.readAsDataURL(file);
}

$('#up-info').submit(event => {
    event.preventDefault();
    userInfoUpDto.username = $('#username').val();
    userInfoUpDto.email = $('#email').val();
    userInfoUpDto.userpassword = $('#password').val();
    const currPass = prompt('Your current password is:');
    if (!currPass) {
        alert('complete your password!');
        return;
    }
    fetch(`/api/change/${currPass}`, {
        method: 'PUT',
        cache: 'no-cache',
        credentials: 'same-origin',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(userInfoUpDto)
    })
    .then(res => {
        if (res.status !== 200) {
            alert('error!');
            return;
        }
        window.location.reload();
    }).catch(err => {
        alert('error!')
        console.error(err);
    });
})