//添加事件监听器，监听表单的提交事件,阻止表单的默认提交行为
document.getElementById('loginForm').addEventListener('submit', function(event) {
    
    //获取表单数据
    event.preventDefault();
    
    // 获取触发事件的表单
    const formData = new FormData(event.target);
    const data = new URLSearchParams();

    for (const pair of formData) {
        data.append(pair[0], pair[1]);
    }

    fetch('http://localhost:8081/login', {
        method: 'POST',
        body: data,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
    .then(response => {
        if (response.ok) {
            return response.json();
        }
        throw new Error('Network response was not ok');
    })
    .then(data => {
        alert('Login successful: ' + data.message);
        window.location.href = '/';
    })
    .catch(error => {
        alert('Login failed: ' + error.message);
    });
});
// console.log("This script has been loaded and executed.");
// 添加事件监听器，监听验证码的点击事件
updateCaptcha()
function updateCaptcha() {
    const captchaElement = document.getElementById('captcha');
    if (captchaElement) {
        const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        let captcha = '';
        for (let i = 0; i < 4; i++) {
            captcha += chars.charAt(Math.floor(Math.random() * chars.length));
        }
        captchaElement.textContent = captcha;
    }
}