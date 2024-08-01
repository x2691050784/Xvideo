# RESTful
* restful架构
* Http方法语义
* http方法幂等性
* restful接口设计原则

## restful架构
* rest指的是一组框架约束与原则
* restful 表述得是资源的状态性转移 <u>*WEB中就是URI*</u>
* 
## HTTP方法的语义
* **GET（幂等性）** :去获取指定对象；
* **DELETE（幂等性）** ：删除指定资源；
* **POST（不具备幂等性）**： 发送数据给服务器，蕴含一下功能：  
  * 1.公告板，新闻组，邮件列表发布消息
  * 2.注册
  * 3.向数据处理程序提供一批数据 <u>表单</u>
  * POST要创建的不是资源本身，有时资源的接受者

* **PUT** 幂等性：调用一次方法与调用次没有副作用

### POST与PUT本质的差别就是幂等性

## PESTful接口命名实例
1. 命名1：HTTP方法后跟的URL必须是名词切同意成名词复数形式
2. 命名2：URL中不采用大小写混合的命名采取全小写，如果需要多个单词则采用“-”来链接  
比如`/user-fans`

## URL分级原则
* 一级用来定位资源分类
* 两级用来定位具体的某个资源  
`/user/20`
### 实例
* 简单型

| URI       | 方法     | 功能         |
|-----------|--------|------------|
| /users    | get    | 获取用列表      |
| /users/20 | 20     | 获取id为20的用户 |
| /users    | POST   | 创建用户       |
| /users/20 | put    | 修改id为20的用户 |
| /users/20 | delete | 删除id为20的用户 |
* 复杂型

| URI                                      | 方法   | 功能                  |
|------------------------------------------|------|---------------------|
| /user?gander=male                        | GET  | 过滤                  |
| /user?sort=created-time-dasc             | GET  | 时间排序                |
| /user?gander=male&sort=created-time-dasc | GET  | 过滤+排序               |
| /user?gander=male                        | GET  | 过滤                  |
| /users?size=10&no=1                      | GET  | 分页 一页展示size条数据 第no页 |

## 常见注解
### @RestController
@RestController 是一个组合注解，它结合了 @Controller 和 @ResponseBody 注解的功能（就相当于把两个注解组合在一起）。在使用 @RestController 注解标记的类中，每个方法的返回值都会以 JSON 或 XML 的形式直接写入 HTTP 响应体中，相当于在每个方法上都添加了 @ResponseBody 注解。


>@RestController
> >@Autowired
### @Controller