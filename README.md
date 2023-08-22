# RabbitMQ

## 1. 基于Docker安装RabbitMQ

### 1.1 搜索镜像

```
docker search rabbitmq:3.12.0-management
```

### 1.2 下载镜像

```
docker pull rabbitmq:3.12.0-management
```

### 1.3 创建并运行容器

```
docker run -d --name rabbitmq3.12.0 -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=12345 rabbitmq:3.12.0-management
```

+ `-d`：后台运行
+ `--name rabbitmq3.12.0`：容器名称
+ `-p 5672:5672`
+ `-p 15672:15672`
+ `-e RABBITMQ_DEFAULT_USER=admin`：管理用户
+ `-e RABBITMQ_DEFAULT_PASS=12345`：管理用户名
+ `rabbitmq:3.12.0-management`：镜像

### 1.4 查看运行的容器

```
docker ps
```

### 1.5 登录验证

在浏览器中输入`http://<ip>:15672`, 输入用户名`admin`，输入密码`12345`, 成功登录。

![RabbitMQ Login Page](.\images\md\rabbitmq-login.jpg)

