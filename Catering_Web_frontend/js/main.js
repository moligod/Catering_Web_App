const routes = {
    '/': 'public/home.html',
    '/login': 'public/login.html',
    '/register': 'public/register.html',
    '/houtai': 'public/houtai.html',
    '/index.html': 'public/home.html',
};
//函数用于拦截浏览器的导航事件，更新浏览器的历史记录，并调用handleLocation来动态加载页面内容。
function navigate(event, path) {
    event.preventDefault();
    window.history.pushState({}, path, window.location.origin + path);
    handleLocation();
}

//函数根据当前的路径查找对应的HTML文件，并通过fetchAPI加载内容到#content容器中。
function handleLocation() {
    const path = window.location.pathname;
    const route = routes[path] || 'public/404.html';
    fetch(route)
        .then(response => {
            if (!response.ok) {
                return fetch('public/404.html');
            }
            return response;
        })
        .then(response => response.text())
        .then(html => {
            const contentDiv = document.getElementById('content');
            contentDiv.innerHTML = html;

            if(path != '/') {
            // 执行页面中的所有脚本(因为动态加载的页面中的脚本不会自动执行)
            Array.from(contentDiv.querySelectorAll("script")).forEach(oldScript => {
                const newScript = document.createElement("script");
                Array.from(oldScript.attributes).forEach(attr => newScript.setAttribute(attr.name, attr.value));
                newScript.appendChild(document.createTextNode(oldScript.innerHTML));
                oldScript.parentNode.replaceChild(newScript, oldScript);
            });
        }
        })
        .catch(() => {
            document.getElementById('content').innerHTML = '<h2>404 Not Found</h2><p>The page you are looking for does not exist.</p>';
        });
}
//浏览器历史记录发生变化时触发handleLocation函数(比如点击浏览器的前进或后退按钮)
window.onpopstate = handleLocation;
//页面加载完成时触发handleLocation函数(基于当前的URL初始化页面内容)
window.onload = handleLocation;

