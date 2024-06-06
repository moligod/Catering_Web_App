const routes = {
    '/': 'public/home.html',
    '/login': 'public/login.html',
    '/register': 'public/register.html',
    '/admin': 'public/admin.html',
    '/index.html': 'public/home.html',
};

//获取指定cookie的值
function getCookie(name) {
    // 使用正则表达式匹配Cookie字符串中的指定名称的值
    var match = document.cookie.match(new RegExp('(^| )' + name + '=([^;]+)'));
    // 如果匹配成功，返回Cookie值
    if (match) return match[2];
    // 如果没有找到匹配的Cookie值，返回null
    return null;
}

//验证Token函数
function verifyToken(path, callback) {
    // 获取名为'jwtToken'的Cookie值
    var token = getCookie('jwtToken');
    // 如果没有找到直接重定向到登录界面
    if (!token) {
        // console.log('2');
        window.location.href = "/login";
        return;
    }
    // console.log("path地址"+path);
    //发送一个post请求到后端验证token
    fetch("http://localhost:8081"+path, {
        method: 'POST',
        credentials: 'include' // 确保发送请求时包含Cookie
    })
        .then(response => {
            if (response.status === 200) {
                // Token验证成功
                callback();
            } else {
                // Token验证失败，跳转到登录页面
                window.location.href = "/login";
            }
        })
        .catch(error => {
            // 捕捉请求过程中的错误，并重定向到登录页面
            window.location.href = "/login";
            console.error('Error:', error);
        });
}

// 导航函数，用于拦截浏览器的导航事件
function navigate(event, path) {
    event.preventDefault();
    window.history.pushState({}, path, window.location.origin + path);
    handleLocation();
}

//处理页面加载的函数
function handleLocation() {
    const path = window.location.pathname;
    const route = routes[path] || 'public/404.html';
    if (routes.hasOwnProperty(path)) {
        // 不需要验证Token的路径,登录，注册，首页
        const protectedPaths = ['/login', '/register', '/'];

        if (protectedPaths.includes(path)) {
            // 不需要验证Token，直接加载页面内容
            loadPage(route);
        } else {
            //验证是否有Token
            verifyToken(path, () => {
                // 验证成功后加载页面内容
                loadPage(route);
            });
        }
    } else {
        // 如果路径不在定义的路由对象中，加载404页面
        loadPage('public/404.html');
    }
}

//加载页面内容的函数
function loadPage(route) {
    // 使用fetch API加载指定的HTML文件，响应成功就返回，不成功就404界面
    fetch(route)
        .then(response => {
            if (!response.ok) {
                return fetch('public/404.html');
            }
            return response;
        })
        .then(response => response.text())// 将响应对象转换为文本
        .then(html => {
            // 获取内容容器的引用，将HTML内容插入其中
            const contentDiv = document.getElementById('content');
            // 将加载的HTML内容插入到内容容器中
            contentDiv.innerHTML = html;
            // 如果加载的不是主页
            if (route !== 'public/home.html') {
                // 执行页面中的所有脚本(因为动态加载的页面中的脚本不会自动执行)
                Array.from(contentDiv.querySelectorAll("script")).forEach(oldScript => {
                    // 创建一个新的script元素
                    const newScript = document.createElement("script");
                    // 复制旧的script元素的所有属性到新的script元素
                    Array.from(oldScript.attributes).forEach(attr => newScript.setAttribute(attr.name, attr.value));
                    // 将旧的script元素的“内容”复制到新的script元素
                    newScript.appendChild(document.createTextNode(oldScript.innerHTML));
                    // 用新的script元素替换旧的script元素
                    oldScript.parentNode.replaceChild(newScript, oldScript);
                });
            }
        })
        .catch(() => {
            // 如果发生错误，重定向到404页面
            window.location.href = '/404.html';
            // 旧版：如果发生错误，显示404错误信息
            // document.getElementById('content').innerHTML = '<h2>404 Not Found</h2><p>The page you are looking for does not exist.</p>';
        });
}

// 浏览器历史记录发生变化时触发handleLocation函数(比如点击浏览器的前进或后退按钮)
window.onpopstate = handleLocation;
// 页面加载完成时触发handleLocation函数(基于当前的URL初始化页面内容)
window.onload = handleLocation;
