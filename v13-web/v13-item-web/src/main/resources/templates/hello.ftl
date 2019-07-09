<!DOCTYPE html>
<html lang="en">
<!--freemarker生成静态页面-->
<head>
    <meta charset="UTF-8">
    <title>Title</title>
</head>
<body>
    hello!${username}
    <hr/>

    <#--传输对象-->
    id:${friends.id}
    name:${friends.name}
    <#--
    entryDate:${friends.entryDate} 错误的 -->
    entryDate:${friends.entryDate?date}
    entryDate:${friends.entryDate?time}
    entryDate:${friends.entryDate?datetime}

    <hr/>
    <#--遍历结合-->
    <h1>展示集合信息</h1>
    <table>
        <tr>
            <td>ID</td>
            <td>name</td>
            <td>entryDate</td>
        </tr>
        <#list list as fri>
            <tr>
                <td>${fri.id}</td>
                <td>${fri.name}</td>
                <td>${fri.entryDate?datetime}</td>
            </tr>
        </#list>
    </table>

    <hr/>
    <#--if判断-->
    <#if (money>200000)>
        吃瘦肉粥
        <#elseif (money>=100000)>
        青菜粥
        <#else>
        白粥
    </#if>

    <hr/>
    <#--null的处理-->
    ${msg!}
    ${msg!'没有任何数据'}

</body>
</html>