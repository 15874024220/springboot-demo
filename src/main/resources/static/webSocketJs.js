/**
 * 执行方式：复制到浏览器开发工具的console中回车执行
 * @type {WebSocket}
 */

// 建立WebSocket连接
const ws = new WebSocket('ws://localhost:8080/api/websocket/123');

ws.onopen = function() {
    console.log('连接成功');
    // 发送消息
    ws.send('Hello, Server!');
};

ws.onmessage = function(event) {
    console.log('收到消息:', event.data);
};

ws.onclose = function() {
    console.log('连接关闭');
};

ws.onerror = function(error) {
    console.log('发生错误:', error);
};

// 通过HTTP API发送消息
// 发送单点消息
fetch('/api/websocket/send/one', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify({
        userId: '123',
        message: 'Hello, User!'
    })
});

// 发送广播消息
fetch('/api/websocket/send/broadcast', {
    method: 'POST',
    headers: {
        'Content-Type': 'application/json'
    },
    body: JSON.stringify({
        message: '广播消息'
    })
});