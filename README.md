Spring Boot 项目结构

springboot-demo/
├── pom.xml
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── example/
│   │   │           └── demo/
│   │   │               ├── SpringbootDemoApplication.java      # 主启动类
│   │   │               ├── controller/                          # 控制器层
│   │   │               │   └── HelloController.java
│   │   │               ├── service/                             # 业务层
│   │   │               │   └── UserService.java
│   │   │               ├── repository/                          # 数据访问层
│   │   │               │   └── UserRepository.java
│   │   │               ├── entity/                              # 实体类
│   │   │               │   └── User.java
│   │   │               └── config/                              # 配置类
│   │   │                   └── WebConfig.java
│   │   └── resources/
│   │       ├── application.yml                                  # 配置文件
│   │       ├── static/                                          # 静态资源
│   │       └── templates/                                       # 模板文件
│   └── test/
│       └── java/
│           └── com/
│               └── example/
│                   └── demo/
│                       └── SpringbootDemoApplicationTests.java
└── target/                                                      # 编译输出