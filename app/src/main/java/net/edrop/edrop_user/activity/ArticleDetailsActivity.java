package net.edrop.edrop_user.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import com.zyao89.view.zloading.ZLoadingDialog;
import com.zyao89.view.zloading.Z_TYPE;

import net.edrop.edrop_user.R;
import net.edrop.edrop_user.utils.Constant;
import net.edrop.edrop_user.utils.SystemTransUtil;

import org.markdown4j.Markdown4jProcessor;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ArticleDetailsActivity extends AppCompatActivity {

    private ImageView finishActivity;
    private ZLoadingDialog dialog;
    private WebView webView;
    private OkHttpClient okHttpClient;
    private String markdown = "---\n" +
            "title: 1-Mybatis框架\n" +
            "date: 2020-03-17 11:22:59\n" +
            "categories: Mybatis\n" +
            "tags: 学习\n" +
            "---\n" +
            "\n" +
            "## Mybatis使用流程\n" +
            "1. 导入jar包\n" +
            "2. 编写mybatis主配置文件xml\n" +
            "3. 创建实体类和映射接口（注意是接口）\n" +
            "4. 编写mybatis的sql映射文件\n" +
            "5. 主配置文件与SQL映射文件相关联\n" +
            "6. 进行编码\n" +
            "\n" +
            "<!--more-->\n" +
            "\n" +
            "## MyBatis主配置文件\n" +
            "```xml\n" +
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<!DOCTYPE configuration PUBLIC \"-//mybatis.org//DTD Config 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-config.dtd\"> \n" +
            "<configuration>\n" +
            "    \n" +
            "    <!-- 通用设置：延迟加载、缓存、日志显示方式 -->\n" +
            "    <settings>\n" +
            "         <!-- 输出SQL语句，STDOUT_LOGGING控制台显示 -->\n" +
            "         <!-- logImpl设置用来指定 MyBatis 所用日志的具体实现，未指定时将自动查找 -->\n" +
            "         <setting name=\"logImpl\" value=\"STDOUT_LOGGING\"/>\n" +
            "    </settings>\n" +
            "    \n" +
            "    <!-- 数据库环境 -->\n" +
            "    <environments default=\"development\">\n" +
            "         <environment id=\"development\">\n" +
            "              <!-- 数据库事务的管理方式和连接数据库参数 -->\n" +
            "              <!-- JDBC这种方式是直接使用了JDBC的事务提交和回滚设置 -->\n" +
            "              <!-- MANAGED(托管)让容器来管理事务的整个生命周期(比如 Spring) -->\n" +
            "              <transactionManager type=\"JDBC\"/>\n" +
            "              <!-- POOLED：这是JDBC连接对象的数据库连接池的实现,用来避免创建新的连接实例 -->\n" +
            "              <!-- UNPOOLED: 每次被请求时简单打开和关闭连接 -->\n" +
            "              <!-- JNDI：这个数据源的实现是为了使用如Spring或应用服务器这类的容器 -->\n" +
            "              <dataSource type=\"POOLED\">\n" +
            "                  <property name=\"driver\" value=\"com.mysql.jdbc.Driver\"/>\n" +
            "                  <property name=\"url\" value=\"jdbc:mysql://localhost:3306/mybatis_demo?characterEncoding=utf-8\"/>\n" +
            "                  <property name=\"username\" value=\"root\"/>\n" +
            "                  <property name=\"password\" value=\"\"/>\n" +
            "              </dataSource>\n" +
            "         </environment>\n" +
            "    </environments>\n" +
            "    \n" +
            "    <!-- 添加映射文件位置 -->\n" +
            "    <mappers>\n" +
            "        <!-- 添加单个映射文件 -->\n" +
            "        <!-- <mapper resource=\"net/onest/mapper/UserMapper.xml\"/> -->\n" +
            "\t\t<!-- 使用映射器接口的完全限定类名 -->\n" +
            "\t\t<!-- <mapper class=\"org.mybatis.builder.AuthorMapper\"/> -->\n" +
            "        <!-- 添加整个包中的映射文件 -->\n" +
            "        <package name=\"net.onest.mapper\"/>\n" +
            "    </mappers>\n" +
            "</configuration>\n" +
            "```\n" +
            "## MyBatis主要的类层次结构\n" +
            "```java\n" +
            "public class MyBatisUtil {\n" +
            "\tprivate static SqlSessionFactory factory;\n" +
            "\tstatic {\n" +
            "\t\ttry {\n" +
            "\t\t\tInputStream is =\n" +
            "\t\t\t\tResources.getResourceAsStream(\"mybatis.xml\");\n" +
            "\t\t\tfactory = new SqlSessionFactoryBuilder().build(is);\n" +
            "\t\t\tis.close();\n" +
            "\t\t} catch (IOException e) {\n" +
            "\t\t\te.printStackTrace();\n" +
            "\t\t}\n" +
            "\t}\n" +
            "\tpublic static SqlSession openSqlSession() {\n" +
            "\t\treturn factory.openSession();\n" +
            "\t}\n" +
            "}\n" +
            "```\n" +
            "\n" +
            "## MyBatis映射文件\n" +
            "\n" +
            "```xml\n" +
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\">\n" +
            "\n" +
            "<!-- namespace:映射器接口的完全限定名,方法为：insert，update，delete，select -->\n" +
            "<mapper namespace=\"net.onest.mapper.UserMapper\">\n" +
            "\n" +
            "    <!-- 结果映射 -->\n" +
            "    <resultMap type=\"net.onest.entity.User\" id=\"userMap\">\n" +
            "        <id column=\"id\" property=\"id\"></id>\n" +
            "        <result column=\"user_name\" property=\"userName\"/>\n" +
            "        <result column=\"password\" property=\"password\"/>\n" +
            "    </resultMap>\n" +
            "    \n" +
            "    <!-- 映射select语句 -->\n" +
            "    <!-- resultType是映射的类 ， resultMap 必须在上面声明映射结果-->\n" +
            "    <!-- <select id=\"findAllUsers\"  resultType=\"net.onest.entity.User\"> -->\n" +
            "    <select id=\"findAllUsers\"  resultMap=\"userMap\">\n" +
            "           select * from user\n" +
            "    </select>\n" +
            "    \n" +
            "    <!-- 插入操作 -->\n" +
            "    <!-- useGeneratedKeys和keyProperties属性，用来获取数据库中的主键的-->\n" +
            "    <insert id=\"insertUser\" parameterType=\"net.onest.entity.User\" useGeneratedKeys=\"true\" keyProperty=\"id\">\n" +
            "        insert into user(id, user_name, password)\n" +
            "        values(#{id}, #{userName}, #{password})\n" +
            "    </insert>\n" +
            "</mapper>\n" +
            "```\n" +
            "## 映射器接口类,实体User类\n" +
            "```java\n" +
            "package net.onest.mapper;\n" +
            "\n" +
            "import java.util.List;\n" +
            "\n" +
            "import net.onest.entity.User;\n" +
            "\n" +
            "public interface UserMapper {\n" +
            "\n" +
            "\tpublic List<User> findAllUsers();\n" +
            "\t\n" +
            "\tpublic int insertUser(User user);\n" +
            "}\n" +
            "\n" +
            "```\n" +
            "\n" +
            "```java\n" +
            "package net.onest.entity;\n" +
            "\n" +
            "public class User {\n" +
            "\n" +
            "\tprivate int id;\n" +
            "\tprivate String userName;\n" +
            "\tprivate String password;\n" +
            "\t......\n" +
            "}\n" +
            "```\n" +
            "## 测试类\n" +
            "\n" +
            "```java\n" +
            "package net.onest.ui;\n" +
            "\n" +
            "import java.util.List;\n" +
            "\n" +
            "import org.apache.ibatis.session.SqlSession;\n" +
            "\n" +
            "import net.onest.entity.User;\n" +
            "import net.onest.mapper.UserMapper;\n" +
            "import net.onest.util.MyBatisUtil;\n" +
            "\n" +
            "public class Test {\n" +
            "\n" +
            "\tpublic static void main(String[] args) {\n" +
            "\n" +
            "\t\t//第一种方式（传统方式）\n" +
            "\t\t//打开SqlSession\n" +
            "//\t\tSqlSession sqlSession = MyBatisUtil.openSqlSession();\n" +
            "//\t\t//参数为映射文件中的namespace+id\n" +
            "//\n" +
            "//\t\tUser user = new User();\n" +
            "//\t\tuser.setUserName(\"pp\");\n" +
            "//\t\tuser.setPassword(\"456\");\n" +
            "//\t\tsqlSession.insert(\"net.onest.mapper.UserMapper.insertUser\", user);\n" +
            "//\t\tsqlSession.commit();\n" +
            "//\t\t\n" +
            "//\t\tList<User> users = sqlSession.selectList(\"net.onest.mapper.UserMapper.findAllUsers\");\n" +
            "//\t\tSystem.out.println(users);\n" +
            "//\t\t\n" +
            "//\t\t//关闭SqlSession\n" +
            "//\t\tsqlSession.close();\n" +
            "\t\t\n" +
            "\t\t//第二种方式（推荐使用）\n" +
            "\t\t//打开SqlSession\n" +
            "\t\tSqlSession sqlSession = MyBatisUtil.openSqlSession();\n" +
            "\t\t//得到了映射器接口实现类的对象\n" +
            "\t\tUserMapper userMapper = sqlSession.getMapper(UserMapper.class);\n" +
            "\t\t\n" +
            "//\t\tUser user = new User();\n" +
            "//\t\tuser.setUserName(\"zj\");\n" +
            "//\t\tuser.setPassword(\"456\");\n" +
            "//\t\tuserMapper.insertUser(user);\n" +
            "//\t\tsqlSession.commit();\n" +
            "\t\t\n" +
            "\t\tList<User> users =  userMapper.findAllUsers();\n" +
            "\t\tSystem.out.println(users);\n" +
            "\t\t//关闭session\n" +
            "\t\tsqlSession.close();\n" +
            "\t}\n" +
            "\n" +
            "}\n" +
            "\n" +
            "```\n" +
            "\n" +
            "## MyBatis主要的类层次结构\n" +
            "![](http://hiasenna.cn-bj.ufileos.com/bcdcf829-2aa7-4936-a0f8-45a3ab1f4f58.jpg?UCloudPublicKey=TOKEN_6af2b312-5e90-4f3b-b1fb-b9ace08fc14f&Signature=tt8cOOjdo6U%2B0NailGnTYlhKodk%3D&Expires=1906527281)\n" +
            "\n" +
            "## SqlSession的工作流程\n" +
            "\n" +
            "1. 开启一个数据库访问会话---创建SqlSession对象：MyBatis封装了对数据库的访问，把对数据库的会话和事务控制放到了SqlSession对象中。\n" +
            "![](http://hiasenna.cn-bj.ufileos.com/3be71158-16b2-416d-bb4d-183130c73159.jpg?UCloudPublicKey=TOKEN_6af2b312-5e90-4f3b-b1fb-b9ace08fc14f&Signature=thP3ohLAwzYrnlREv4IhTALmnqQ%3D&Expires=1906527311)\n" +
            "2. 为SqlSession传递一个映射的SQL语句的StatementId和参数，然后返回结果：\n" +
            "\t1. SqlSession根据Statement ID, 在MyBatis配置对象\n" +
            "\t2. Configuration中获取到对应的MappedStatement对象\n" +
            "\t3. 调用MyBatis执行器来执行具体的操作。\n" +
            "3. MyBatis在初始化的时候，会将MyBatis的配置信息全部加载到内存中，使用Configuration实例来维护。\n" +
            "4. 映射文件加载到内存中会生成n个对应的MappedStatement对象\n" +
            "\n";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        new SystemTransUtil().trans(ArticleDetailsActivity.this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);
        initView();
        initData();
        initOption();
        setListener();
    }

    private void setListener() {
        finishActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initOption() {
        try {
            String html5 = new Markdown4jProcessor().process(markdown);
            webView.loadDataWithBaseURL(null, html5, "text/html", "UTF-8", null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initView() {
        dialog = new ZLoadingDialog(ArticleDetailsActivity.this);
        okHttpClient = new OkHttpClient();
        webView = findViewById(R.id.web_view);
        finishActivity = findViewById(R.id.iv_article_details_finish);
    }

    private void initData() {
        dialog.setLoadingBuilder(Z_TYPE.DOUBLE_CIRCLE)//设置类型
                .setLoadingColor(Color.parseColor("#00FF7F"))
                .setHintText("正在加载中...")
                .setHintTextColor(Color.GRAY)
                .setDialogBackgroundColor(Color.parseColor("#cc111111"))
                .show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        Intent intent = getIntent();
        int articleId = intent.getIntExtra("articleId", 0);
        sendMessageByArticleId(articleId);
        dialog.cancel();
    }

    private void sendMessageByArticleId(int articleId) {
        FormBody formBody = new FormBody.Builder()
                .add("articleId", String.valueOf(articleId))
                .build();
        Request request = new Request.Builder()
                .url(Constant.NEWS_URL + "community/get_article_detail_info")
                .post(formBody)
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String markdown22 = response.body().string();
                Log.e("markdown", markdown);
            }
        });
    }
}
