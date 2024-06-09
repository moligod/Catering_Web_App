layui.use(['form', 'layer'], function() {
    var form = layui.form;
    var layer = layui.layer;
});

function showAddD-------------------------------------------------------------------------------------------------------------------------iishForm() {
    var content = `
        <form class="layui-form" action="">
            <div class="layui-form-item">
                <label class="layui-form-label">菜品名称</label>
                <div class="layui-input-block">
                    <input type="text" name="name" required lay-verify="required" placeholder="请输入菜品名称" autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">价格</label>
                <div class="layui-input-block">
                    <input type="text" name="price" required lay-verify="required" placeholder="请输入价格" autocomplete="off" class="layui-input">
                </div>
            </div>
            <div class="layui-form-item">
                <label class="layui-form-label">描述</label>
                <div class="layui-input-block">
                    <textarea name="description" placeholder="请输入描述" class="layui-textarea"></textarea>
                </div>
            </div>
            <div class="layui-form-item">
                <div class="layui-input-block">
                    <button class="layui-btn" lay-submit lay-filter="addDish">添加</button>
                </div>
            </div>
        </form>
    `;
    document.getElementById('content').innerHTML = content;
    layui.form.render();
}

function showDishes() {
    var content = '<h2>菜品列表</h2><p>这里显示菜品列表。</p>';
    document.getElementById('content').innerHTML = content;
}
