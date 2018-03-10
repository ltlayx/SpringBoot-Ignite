# Spring Boot整合JWT实现用户认证

之前初学了一下Spring Boot和JWT的内容，写了几篇小文章，但是杂乱无章，就重新整理了一下自己学习的东西，尽量写的足够详细，给像我一样刚刚接触这个内容的新手一个参考。这里附上代码的[Github源码地址](https://github.com/ltlayx/SpringBoot-Ignite) ，参考的文献也附在这里[^footnote1][^footnote2]

## 初探JWT
1. 什么是JWT
	JWT(Json Web Token)，是一种工具，格式为` XXXX.XXXX.XXXX `的字符串，JWT以一种安全的方式在用户和服务器之间传递存放在JWT中的不敏感信息。
2. 为什么要用JWT
	设想这样一个场景，在我们登录一个网站之后，再把网页或者浏览器关闭，下一次打开网页的时候可能显示的还是登录的状态，不需要再次进行登录操作，通过JWT就可以实现这样一个用户认证的功能。当然使用Session可以实现这个功能，但是使用Session的同时也会增加服务器的存储压力，而JWT是将存储的压力分布到各个客户端机器上，从而减轻服务器的压力。
3. JWT长什么样
	JWT由3个子字符串组成，分别为Header，Payload以及Signature，结合JWT的格式即：` Header.Payload.Signature `。（Claim是描述Json的信息的一个Json，将Claim转码之后生成Payload）。
	- Header
		Header是由以下这个格式的Json通过Base64编码（编码不是加密，是可以通过反编码的方式获取到这个原来的Json，所以JWT中存放的一般是不敏感的信息）生成的字符串，Header中存放的内容是说明编码对象是一个JWT以及使用“SHA-256”的算法进行加密（加密用于生成Signature）
	```
	{
		"typ":"JWT",
		"alg":"HS256"
	}
	```
	- Claim
		Claim是一个Json，Claim中存放的内容是JWT自身的标准属性，所有的标准属性都是可选的，可以自行添加，比如：JWT的签发者、JWT的接收者、JWT的持续时间等；同时Claim中也可以存放一些自定义的属性，这个自定义的属性就是在用户认证中用于标明用户身份的一个属性，比如用户存放在数据库中的id，为了安全起见，一般不会将用户名及密码这类敏感的信息存放在Claim中。将Claim通过Base64转码之后生成的一串字符串称作Payload。
		```
		{
			"iss":"Issuer —— 用于说明该JWT是由谁签发的",
			"sub":"Subject —— 用于说明该JWT面向的对象",
			"aud":"Audience —— 用于说明该JWT发送给的用户",
			"exp":"Expiration Time —— 数字类型，说明该JWT过期的时间",
			"nbf":"Not Before —— 数字类型，说明在该时间之前JWT不能被接受与处理",
			"iat":"Issued At —— 数字类型，说明该JWT何时被签发",
			"jti":"JWT ID —— 说明标明JWT的唯一ID",
			"user-definde1":"自定义属性举例",
			"user-definde2":"自定义属性举例"
		}
		```
	- Signature
		Signature是由Header和Payload组合而成，将Header和Claim这两个Json分别使用Base64方式进行编码，生成字符串Header和Payload，然后将Header和Payload以` Header.Payload `的格式组合在一起形成一个字符串，然后使用上面定义好的加密算法和一个密匙（这个密匙存放在服务器上，用于进行验证）对这个字符串进行加密，形成一个新的字符串，这个字符串就是Signature。
	- 总结
	![这里写图片描述](http://img.blog.csdn.net/20180310163806888?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbHRsMTEyMzU4/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
4. JWT实现认证的原理
		服务器在生成一个JWT之后会将这个JWT会以` Authorization : Bearer JWT ` 键值对的形式存放在cookies里面发送到客户端机器，在客户端再次访问收到JWT保护的资源URL链接的时候，服务器会获取到cookies中存放的JWT信息，首先将Header进行反编码获取到加密的算法，在通过存放在服务器上的密匙对` Header.Payload ` 这个字符串进行加密，比对JWT中的Signature和实际加密出来的结果是否一致，如果一致那么说明该JWT是合法有效的，认证成功，否则认证失败。

## JWT实现用户认证的流程图
![这里写图片描述](http://img.blog.csdn.net/20180310125157455?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbHRsMTEyMzU4/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

## JWT的代码实现
这里的代码实现使用的是Spring Boot（版本号：1.5.10）框架，以及Apache Ignite（版本号：2.3.0）数据库（有关Ignite和Spring Boot的整合可以查看之前写过的一篇文章 [在Spring Boot上部署ignite数据库的小例子](http://blog.csdn.net/ltl112358/article/details/79399026)）

- 代码说明：
		代码中与JWT有关的内容如下
		config包中` JwtCfg `类配置生成一个JWT并配置了JWT拦截的URL
		controller包中` PersonController ` 用于处理用户的登录注册时生成JWT，` SecureController ` 用于测试JWT
		model包中` JwtFilter ` 用于处理与验证JWT的正确性 
		其余的是属于Ignite数据库访问的相关内容
		![这里写图片描述](http://img.blog.csdn.net/2018031013062318?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbHRsMTEyMzU4/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
- ` JwtCfg ` 类
		这个类中声明了一个` @Bean ` 	，用于生成一个过滤器类，对` /secure` 链接下的所有资源访问进行JWT的验证 
```
/**
 * This is Jwt configuration which set the url "/secure/*" for filtering
 * @program: users
 * @author: 李泰郎
 * @create: 2018-03-03 21:18
 **/
@Configuration
public class JwtCfg {

    @Bean
    public FilterRegistrationBean jwtFilter() {
        final FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(new JwtFilter());
        registrationBean.addUrlPatterns("/secure/*");

        return registrationBean;
    }

}
```
- ` JwtFilter` 类
		这个类声明了一个JWT过滤器类，从Http请求中提取JWT的信息，并使用了"secretkey"这个密匙对JWT进行验证
```
/**
 * Check the jwt token from front end if is invalid
 * @program: users
 * @author: 李泰郎
 * @create: 2018-03-01 11:03
 **/
public class JwtFilter extends GenericFilterBean {

    public void doFilter(final ServletRequest req, final ServletResponse res, final FilterChain chain)
            throws IOException, ServletException {

        // Change the req and res to HttpServletRequest and HttpServletResponse
        final HttpServletRequest request = (HttpServletRequest) req;
        final HttpServletResponse response = (HttpServletResponse) res;

        // Get authorization from Http request
        final String authHeader = request.getHeader("authorization");

        // If the Http request is OPTIONS then just return the status code 200
        // which is HttpServletResponse.SC_OK in this code
        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);

            chain.doFilter(req, res);
        }
        // Except OPTIONS, other request should be checked by JWT
        else {

            // Check the authorization, check if the token is started by "Bearer "
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new ServletException("Missing or invalid Authorization header");
            }

            // Then get the JWT token from authorization
            final String token = authHeader.substring(7);

            try {
                // Use JWT parser to check if the signature is valid with the Key "secretkey"
                final Claims claims = Jwts.parser().setSigningKey("secretkey").parseClaimsJws(token).getBody();

                // Add the claim to request header
                request.setAttribute("claims", claims);
            } catch (final SignatureException e) {
                throw new ServletException("Invalid token");
            }

            chain.doFilter(req, res);
        }
    }
}
```
- ` PersonController` 类
	这个类中在用户进行登录操作成功之后，将生成一个JWT作为返回
```
/**
 * @program: users
 * @author: 李泰郎
 * @create: 2018-02-27 19:28
 **/
@RestController
public class PersonController {

    @Autowired
    private PersonService personService;


    /**
     * User register with whose username and password
     * @param reqPerson
     * @return Success message
     * @throws ServletException
     */
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestBody() ReqPerson reqPerson) throws ServletException {
        // Check if username and password is null
        if (reqPerson.getUsername() == "" || reqPerson.getUsername() == null
                || reqPerson.getPassword() == "" || reqPerson.getPassword() == null)
            throw new ServletException("Username or Password invalid!");

        // Check if the username is used
        if(personService.findPersonByUsername(reqPerson.getUsername()) != null)
            throw new ServletException("Username is used!");

        // Give a default role : MEMBER
        List<Role> roles = new ArrayList<Role>();
        roles.add(Role.MEMBER);

        // Create a person in ignite
        personService.save(new Person(reqPerson.getUsername(), reqPerson.getPassword(), roles));
        return "Register Success!";
    }

    /**
     * Check user`s login info, then create a jwt token returned to front end
     * @param reqPerson
     * @return jwt token
     * @throws ServletException
     */
    @PostMapping
    public String login(@RequestBody() ReqPerson reqPerson) throws ServletException {
        // Check if username and password is null
        if (reqPerson.getUsername() == "" || reqPerson.getUsername() == null
                || reqPerson.getPassword() == "" || reqPerson.getPassword() == null)
            throw new ServletException("Please fill in username and password");

        // Check if the username is used
        if(personService.findPersonByUsername(reqPerson.getUsername()) == null
                || !reqPerson.getPassword().equals(personService.findPersonByUsername(reqPerson.getUsername()).getPassword())){
            throw new ServletException("Please fill in username and password");
        }

        // Create Twt token
        String jwtToken = Jwts.builder().setSubject(reqPerson.getUsername()).claim("roles", "member").setIssuedAt(new Date())
                .signWith(SignatureAlgorithm.HS256, "secretkey").compact();

        return jwtToken;
    }
}
```
- ` SecureController` 类
		这个类中只是用于测试JWT功能，当用户认证成功之后，` /secure` 下的资源才可以被访问
```
/**
 * Test the jwt, if the token is valid then return "Login Successful"
 * If is not valid, the request will be intercepted by JwtFilter
 * @program: users
 * @author: 李泰郎
 * @create: 2018-03-01 11:05
 **/
@RestController
@RequestMapping("/secure")
public class SecureController {

    @RequestMapping("/users/user")
    public String loginSuccess() {
        return "Login Successful!";
    }
    
}
```

## 代码功能测试
本例使用Postman对代码进行测试，这里并没有考虑到安全性传递的明文密码，实际上应该用SSL进行加密
1. 首先进行一个新的测试用户的注册，可以看到注册成功的提示返回
	![这里写图片描述](http://img.blog.csdn.net/20180310132536199?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbHRsMTEyMzU4/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
2. 再让该用户进行登录，可以看到登录成功之后返回的JWT字符串
![这里写图片描述](http://img.blog.csdn.net/20180310132824675?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbHRsMTEyMzU4/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
3. 直接申请访问` /secure/users/user` ，这时候肯定是无法访问的，服务器返回500错误
![这里写图片描述](http://img.blog.csdn.net/20180310132926995?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbHRsMTEyMzU4/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)
4. 将获取到的JWT作为Authorization属性提交，申请访问` /secure/users/user` ，可以访问成功
![这里写图片描述](http://img.blog.csdn.net/20180310133041807?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvbHRsMTEyMzU4/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70)

---
[^footnote1]: John Wu: JSON Web Token-在Web应用间安全地传递信息[EB/OL].[2018-03-02].http://blog.leapoahead.com/2015/09/06/understanding-jwt/ 

[^footnote2]: Aboullaite Mohammed: Spring Boot token authentication using JWT[EB/OL]. [2018-03-02].https://aboullaite.me/spring-boot-token-authentication-using-jwt/ ↩
