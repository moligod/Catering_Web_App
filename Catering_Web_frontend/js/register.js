// 全局变量存储当前验证码
let currentCaptcha = '';
//初始化验证码
updateCaptcha()
//监听表单的提交事件
document.getElementById('registerForm').addEventListener('submit', function(event) {
    
    //阻止表单默认提交行为
    event.preventDefault();

    // 获取用户输入的验证码，并转为小写
    const userInput = document.getElementById('captchaInput').value.toLowerCase();

    // 生成的验证码转为小写
    const validCaptcha = currentCaptcha.toLowerCase(); 

    // 比较用户输入的验证码和当前验证码
    if (userInput !== validCaptcha) { 
        Qmsg.error("验证码错误,请重新输入验证码")
        updateCaptcha();
        return;
    }

    // 获取触发事件的表单
    const formData = new FormData(event.target);

    //将表单数据转为URLSearchParams对象
    const data = new URLSearchParams();
    for (const pair of formData) {
        data.append(pair[0], pair[1]);
    }

    // 发送注册请求
    fetch('http://localhost:8081/register', {
        method: 'POST',
        credentials: 'include',//发送凭证，不写接受的cookie会被浏览器拦截（被折磨了好几个小时）
        body: data,
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    })
    .then(response => {
        // 如果请求成功，解析JSON
        if (response.ok) {
            return response.json();
        }
        // 如果响应状态码不是2xx, 解析JSON以获取错误详情
        return response.json().then(errData => {
        // 使用从后端获取的错误消息，如果没有则使用默认消息，抛出错误后执行catch方法
        throw new Error(errData.message || "未知错误");
        });
    })
    .then(data => {
        // 如果登录成功，弹出提示框，并跳转到后台
        // localStorage.setItem('jwtToken', data.data.jwtToken);  // 存储 JWT Token
        Qmsg.success(data.message)
        console.log(document.cookie);  // 打印所有可访问的Cookies
        window.location.href = '/login';
    })
    .catch(error => {
        Qmsg.error(error.message);
    });
});

// 监听验证码的点击事件，点击验证码时更新验证码
document.getElementById('captcha').addEventListener('click', updateCaptcha);

// 更新验证码方法
function updateCaptcha() {
    const captchaElement = document.getElementById('captcha');
    if (captchaElement) {
        const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
        let captcha = '';
        for (let i = 0; i < 4; i++) {
            captcha += chars.charAt(Math.floor(Math.random() * chars.length));
        }
        captchaElement.textContent = captcha;
        // 保存生成的验证码到全局变量进行验证码校验
        currentCaptcha = captcha;  
    }
}