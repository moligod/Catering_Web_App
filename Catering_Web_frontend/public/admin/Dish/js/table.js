
// 定义了一个名为itemsPerPage的常量，用于设置每页显示的菜单项数量。
const itemsPerPage = 8;
// 定义了一个名为currentPage的变量，用于跟踪当前显示的页码，初始值为1
let currentPage = 1;
//展示的数据，设置数据上限
let menuItems = new Array(999);
//展示的总页数
let totalPages;
//数据总数量
let totalItems;

//获取当前页数的菜单数据
async function viewItems() {

  let url = API_URL + "/admin/getMenuList?" + "page=" + currentPage + "&pagesize=" + itemsPerPage;
  try {
    const response = await fetch(url, {
      method: 'GET',
      credentials: 'include' // 确保发送请求时包含Cookie
    })

    if (!response.ok) {
      window.location.href = "/login";
      console.log('获取失败');
    }
    return response.json();
  } catch (error) {
    // 捕捉请求或解析过程中的错误
    console.error('Error:', error);
    window.location.href = "/login";
  }
}

//菜单数总量
async function findmenupages() {
  try {
    const response = await fetch(API_URL + "/admin/getMenupages", {
      method: 'GET',
      credentials: 'include' // 确保发送请求时包含Cookie
    })
    if (!response.ok) {
      window.location.href = "/login";
      console.log('获取失败');
    }
    return response.json();
  } catch (error) {
    // 捕捉请求或解析过程中的错误
    console.error('Error:', error);
  }
}
// 定义了一个名为setCurrentPage的函数，用于设置currentPage的值。因为暴漏出去的是常量
function setCurrentPage(newPage) {
  currentPage = newPage;
}
//外部清空数据，并重新获取渲染
function clearmenuItems() {
  menuItems = [];
  menuItems = new Array(999);
    // 执行到这就代表没有查询过，可以查询了
    viewItems().then(data => {
      // 更新菜单数据到menuItems数组中
      menuItems.splice((currentPage-1) * 8, 0,...data.data);
      // 将menuItems数组的渲染数据到页面
      renderItems(currentPage);
    });
    // 在数据加载并渲染菜单项后渲染分页控件
  renderPagination();

}

// 定义了一个名为renderItems的函数，用于渲染当前页的菜单项。
function renderItems(page) {
  //计算当前页第一条数据的索引。
  const start = page * itemsPerPage - 8;
  // 计算当前页最后一条数据的索引
  const end = start + itemsPerPage;
  // 获取当前页需要显示的菜单属性（按照数组的位置进行收筛选）
  const paginatedItems = menuItems.slice(start, end);
  // 获取表格的<tbody>元素，用于插入菜单项行。
  const tableBody = document.getElementById('menuTable').getElementsByTagName('tbody')[0];
  // 清空<tbody>元素中的所有内容，为新数据的插入做准备。
  tableBody.innerHTML = '';
  //遍历当前页的菜单项，为每个菜单项创建一行并填充数据。
  paginatedItems.forEach(item => {
    //在<tbody>中插入新行，在新行中插入单元格，并设置其文本内容为菜单项的id以此类推，为name、description、price等设置文本内容。
    const row = tableBody.insertRow();
    row.insertCell(0).textContent = item.id;
    row.insertCell(1).textContent = item.name;
    row.insertCell(2).textContent = item.description;
    row.insertCell(3).textContent = item.price;
    row.insertCell(4).textContent = item.category;
    row.insertCell(5).textContent = item.stock;
    row.insertCell(6).textContent = new Date(item.createdAt).toLocaleString();
    row.insertCell(7).textContent = new Date(item.updatedAt).toLocaleString();
  });
}

// 用于渲染分页控件。
function renderPagination() {
  const pagination = document.getElementById('pagination');
  pagination.innerHTML = '';

  // 创建上一页按钮
  const prevButton = document.createElement('button');
  prevButton.textContent = '<';
  // 如果是第一页，则禁用上一页按钮
  prevButton.disabled = currentPage === 1;
  prevButton.addEventListener('click', () => {
    if (currentPage > 1) {
      setCurrentPage(currentPage - 1);
      Updatemenuscreen();
    }
  });
  // 将上一页按钮添加到分页控件中
  pagination.appendChild(prevButton);

  // 计算并创建页码按钮
  const maxButtons = 8; // 显示的按钮数量
  const startPage = Math.max(1, currentPage - Math.floor(maxButtons / 2));
  const endPage = Math.min(totalPages, startPage + maxButtons - 1);
  // 保证当前页始终在中间
  for (let i = startPage; i <= endPage; i++) {
    const pageButton = document.createElement('span');
    pageButton.textContent = i;
    pageButton.className = currentPage === i ? 'page-number active' : 'page-number';
    pageButton.addEventListener('click', () => {
      setCurrentPage(i);
      Updatemenuscreen();
    });
    pagination.appendChild(pageButton);
  }

  // 创建下一页按钮
  const nextButton = document.createElement('button');
  nextButton.textContent = '>';
  nextButton.disabled = currentPage === totalPages; // 如果是最后一页，则禁用下一页按钮
  nextButton.addEventListener('click', () => {
    if (currentPage < totalPages) {
      setCurrentPage(currentPage + 1);
      Updatemenuscreen();
    }
  });
  pagination.appendChild(nextButton);
}

//更新菜单数据
async function Updatemenuscreen() {
  //获取菜单总数量，判断是否有数据被删除或新增
  let menuquantity = await findmenupages();
  for (let i = 0; i < menuItems.length; i++) {
    console.log(menuItems[i]);
  }
  if (menuquantity != totalItems && totalItems != undefined) {
    console.log('数据被改动');
    //清空数据重新获取,清空后记得用length修改数组长度为999否则就是0了
    menuItems = [];
    menuItems.length= 999;
  }
  // 更新总页数
  totalItems = menuquantity;
  // 计算总页数
  totalPages = Math.ceil(totalItems / itemsPerPage);
  // 在数据加载并渲染菜单项后渲染分页控件
  renderPagination();

  //查询是否有数据，如果有数据说明之前查询过，直接渲染
  if(menuItems[currentPage * itemsPerPage - 8]!==undefined){
    console.log("已经查询过了");
    renderItems(currentPage);
    return false;
  }

  // 执行到这就代表没有查询过，可以查询了
  viewItems().then(data => {
    // 更新菜单数据到menuItems数组中
    menuItems.splice((currentPage-1) * 8, 0,...data.data);
    // 将menuItems数组的渲染数据到页面
    renderItems(currentPage);
    // 这里的data就是解析后的JSON数据
    // console.log(data.data.id);

  });
  // 在数据加载并渲染菜单项后渲染分页控件
  // renderPagination();

}
export { menuItems, currentPage, setCurrentPage, totalPages, renderItems, viewItems, findmenupages, renderPagination, Updatemenuscreen,clearmenuItems };