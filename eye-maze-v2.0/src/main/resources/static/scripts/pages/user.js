import "https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.0/jquery.min.js"
let userInfoUpDto = new Object();

let imgInput = document.getElementById('image-input');
$('#image-input').change(e => {
    e.preventDefault();
    if (e.target.files) {
        let imageFile = e.target.files[0];
        let reader = new FileReader();
        reader.onload = e => {
            let img = document.createElement("img");
            img.onload = event => {/*
                // Dynamically create a canvas element
                let canvas = document.createElement("canvas");

                // let canvas = document.getElementById("canvas");
                let ctx = canvas.getContext("2d");

                canvas.width = 47;
                canvas.height = 47;
                // Actual resizing
                ctx.drawImage(img, 0, 0, 47, 47);

                // Show resized image in preview element
                let dataurl = canvas.toDataURL(imageFile.type);//*/
                $("#preview")[0].src = reader.result;
                userInfoUpDto.profilePicB64 = reader.result;
            }
            img.src = e.target.result;
        }
        reader.readAsDataURL(imageFile);
    }
});

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