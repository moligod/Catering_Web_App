document.querySelectorAll('.admin-app-menu-tree').forEach(item => {
  item.addEventListener('click', () => {
    //状态切换,opened下拉
    item.classList.toggle('opened');
    item.classList.toggle('active');
  });
});
document.querySelectorAll('.admin-app-menu-tree+ul').forEach(item => {
  item.addEventListener('click', () => {
    //状态切换,opened下拉
    item.classList.toggle('active');
  });
});


if (document.querySelector('.admin-app-header-user-name').innerText === '') {
  //判断显示名字的地方是否为空，如果为空发送请求，将cookie内的token发送过去，去获取用户名
  fetch("http://localhost:8081/admin/getUserName", {
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
  document.querySelector('.admin-app-header-user').addEventListener('mouseenter', function() {
    document.querySelector('.admin-app-header-user-tree').style.display = 'block'; // 显示下拉菜单
  });

  // 当鼠标离开 .admin-app-header-user 上时
  document.querySelector('.admin-app-header-user').addEventListener('mouseleave', function() {
    setTimeout(function() {
      // 如果鼠标在 .admin-app-header-user-tree或.admin-app-header-user 上，不隐藏下拉菜单
      if (document.querySelector('.admin-app-header-user-tree').matches(':hover') || document.querySelector('.admin-app-header-user').matches(':hover')) {
        return;
      }
      document.querySelector('.admin-app-header-user-tree').style.display = 'none';
    }, 200); 
  });
  // 绑定点击事件到退出登录链接
  document.querySelector('a[href="#logouttoken"]').addEventListener('click', function(event) {
    event.preventDefault(); // 阻止默认的链接行为
    console.log('点击了退出登录链接。');
    logouttoken("Set-Cookie: jwtToken="); // 调用登出函数
  });

  function logouttoken(name) {
    // 发送请求到服务器端的/logout路由来处理cookie的删除
    fetch('http://localhost:8081/logouttoken', {
        method: 'POST', // 或者使用GET，取决于服务器端的实现
        credentials: 'include' // 确保带上credentials，如果需要的话
    }).then(response => {
        if (response.ok) {
            // 登出成功，可以在这里执行额外的登出逻辑
            console.log('您已成功退出登录，正在返回主页');
            // 重定向到主页
            document.cookie = name+'; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/';
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