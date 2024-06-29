//全局插件
//message组件   https://github.com/snwjas/Message.js/tree/master
// 创建 link 标签用于引入CSS样式表
const link = document.createElement('link');
// 设置 link 标签的属性为stylesheet标识这是一个样式表
link.rel = 'stylesheet';
link.href = './lib/message.min.css';
// 将 link 标签插入到 head 中
document.head.appendChild(link);
// 创建 script 标签用于引入js脚本
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


