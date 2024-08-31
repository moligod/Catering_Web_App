// 右侧菜单栏的点击事件
document.querySelectorAll('.admin-app-menu-tree').forEach(item => {
  item.addEventListener('click', () => {
    //状态切换,opened下拉
    item.classList.toggle('opened');
    item.classList.toggle('active');
  });
});
// 右侧菜单栏的点击事件
document.querySelectorAll('.admin-app-menu-tree+ul').forEach(item => {
  item.addEventListener('click', () => {
    //状态切换,opened下拉
    item.classList.toggle('active');
  });
});

// 顶部菜单栏获取用户名
if (document.querySelector('.admin-app-header-user-name').innerText === '') {
  //判断显示名字的地方是否为空，如果为空发送请求，将cookie内的token发送过去，去获取用户名
  fetch(API_URL + "/admin/getUserName", {
    method: 'POST',
    credentials: 'include' // 确保发送请求时包含Cookie
  })
    .then(response => {
      if (response.status === 200) {
        // Token验证成功
        response.json().then(data => {
          //将用户名写入到admin-app-header-user-name中
          document.querySelector('.admin-app-header-user-name').innerText = data.data.username;
        });
      } else {
        // Token验证失败，跳转到登录页面
        window.location.href = "/login";
        console.log('Token验证失败');
      }
    })
    .catch(error => {
      // 捕捉请求过程中的错误，并重定向到登录页面
      window.location.href = "/login";
      console.error('Error:', error);
    });
}

// 当鼠标悬停在 .admin-app-header-user 上时
document.querySelector('.admin-app-header-user').addEventListener('mouseenter', function () {
  document.querySelector('.admin-app-header-user-tree').style.display = 'block'; // 显示下拉菜单
});

// 当鼠标离开 .admin-app-header-user 上时
document.querySelector('.admin-app-header-user').addEventListener('mouseleave', function () {
  setTimeout(function () {
    // 如果鼠标在 .admin-app-header-user-tree或.admin-app-header-user 上，不隐藏下拉菜单
    if (document.querySelector('.admin-app-header-user-tree').matches(':hover') || document.querySelector('.admin-app-header-user').matches(':hover')) {
      return;
    }
    document.querySelector('.admin-app-header-user-tree').style.display = 'none';
  }, 200);
});
// 绑定点击事件到退出登录链接
document.querySelector('a[href="#logouttoken"]').addEventListener('click', function (event) {
  event.preventDefault(); // 阻止默认的链接行为
  console.log('点击了退出登录链接。');
  logouttoken("Set-Cookie: jwtToken="); // 调用登出函数
});

function logouttoken(name) {
  // 发送请求到服务器端的/logout路由来处理cookie的删除
  fetch(API_URL + '/logouttoken', {
    method: 'POST', // 或者使用GET，取决于服务器端的实现
    credentials: 'include' // 确保带上credentials，如果需要的话
  }).then(response => {
    if (response.ok) {
      // 登出成功，可以在这里执行额外的登出逻辑
      console.log('您已成功退出登录，正在返回主页');
      // 重定向到主页
      document.cookie = name + '; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/';
      window.location.href = '/';
    } else {
      // 登出失败，处理错误情况，显示错误信息，并且重新进入admin，进入后会自动校验cookie
      Qmsg.error("退出失败")
      window.location.href = '/admin';
    }
  }).catch(error => {
    // 请求失败，处理网络或其他错误
    Qmsg.error('登出时发生错误：', error);
  });
}

//本地直接加载新增修改删除
function load_edit(paths,content) {
  //子文件中的拟态框
  document.getElementById("myModal").style.display = "block";
  if (paths == "Dishesadd") {
    loadAndExecuteScripts('public/admin/Dish/Dishesadd.html');
  } else if (paths == "Dishesedit") {
    loadAndExecuteScripts('public/admin/Dish/Dishesedit.html',content);
  }
}
//创建一个变量，存放当前模板的路径
let currentTemplatePath = '';
//本地直接加载列表
function Embedded_file(paths) {
  const routes = {
    'Disheslist': 'public/admin/Dish/Disheslist.html',
    'Dishesadd': 'public/admin/Dish/Dishesadd.html',
    'Dishesedit': 'public/admin/Dish/Dishesedit.html',
    'Dishesdelete': 'public/admin/Dish/Dishesdelete.html',
  };
  const route = routes[paths] || 'public/404.html';
  // 如果当前模板路径和要加载的模板路径相同，则不再加载
  if (currentTemplatePath === route) {
    return;
  } else {
    currentTemplatePath = route;
  }
  fetch(route)
    .then(response => response.text())
    .then(html => {
      // 将获取的HTML模板设置为内容容器的innerHTML
      document.getElementById('admin-app-content').innerHTML = html;
      // 执行页面中的所有脚本(因为动态加载的页面中的脚本不会自动执行)
      Array.from(document.getElementById('admin-app-content').querySelectorAll("script")).forEach(oldScript => {
        // 创建一个新的script元素
        const newScript = document.createElement("script");
        // 复制旧的script元素的所有属性到新的script元素
        Array.from(oldScript.attributes).forEach(attr => newScript.setAttribute(attr.name, attr.value));
        // 将旧的script元素的“内容”复制到新的script元素
        newScript.appendChild(document.createTextNode(oldScript.innerHTML));
        // 用新的script元素替换旧的script元素
        oldScript.parentNode.replaceChild(newScript, oldScript);
      });
    });
}

//加载HTML文件并执行脚本
function loadAndExecuteScripts(address, dishData) {
  fetch(address)
    .then(response => response.text())
    .then(html => {
      // 将获取的HTML模板设置为内容容器的innerHTML
      document.querySelector('.modal-content-main').innerHTML = html;
      //如果是修改页面，将菜品数据填充到表单中
      if (address == 'public/admin/Dish/Dishesedit.html') {
        //加载新增菜品的脚本
        document.querySelector('input#name').value = dishData.name || '';
        document.querySelector('input#description').value = dishData.description || '';
        document.querySelector('input#price').value = dishData.price || '';
        document.querySelector('input#category').value = dishData.category || '';
        document.querySelector('input#stock').value = dishData.stock || '';
        document.querySelector('input#id').value = dishData.id || '';
      }
      // 执行页面中的所有脚本(因为动态加载的页面中的脚本不会自动执行)
      const scripts = document.querySelectorAll('.modal-content-main script');
      scripts.forEach(oldScript => {
        // 创建一个新的script元素
        const newScript = document.createElement("script");
        // 复制旧的script元素的所有属性到新的script元素
        for (let attr of oldScript.attributes) {
          newScript.setAttribute(attr.name, attr.value);
        }
        // 将旧的script元素的“内容”复制到新的script元素
        newScript.textContent = oldScript.textContent;
        // 用新的script元素替换旧的script元素
        oldScript.parentNode.replaceChild(newScript, oldScript);
        // 执行新的script元素
        
      });
    })
    .catch(error => {
      console.error('Error fetching the HTML:', error);
    });
}