
//message组件   https://github.com/snwjas/Message.js/tree/master
// 创建 link 标签
const link = document.createElement('link');
link.rel = 'stylesheet';
link.href = './lib/message.min.css';
// 将 link 标签插入到 head 中
document.head.appendChild(link);
// 创建 script 标签
const script = document.createElement('script');
script.src = './lib/message.min.js';
// 将 script 标签插入到 body 中
document.body.appendChild(script);
//设置message的全局配置
window.QMSG_GLOBALS = {
    DEFAULTS: {
        showClose:true,
        timeout: 1000
    }
}
