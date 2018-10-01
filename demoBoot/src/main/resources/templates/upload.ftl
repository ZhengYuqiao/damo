<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport"
          content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <title>Document</title>
</head>
<body>
<h1>FILE UPLOAD</h1>
<form method="post"
      ENCTYPE="multipart/form-data"
      action="/file/upload">
   <label>  文件：
       <input type="file" name="dharmaFile"/>
   </label>
    <input type="submit" value="上传"/>

</form>

    <#if error??>
        <h3>${error}</h3>
    <#elseif  file??>
    <img src="/upload/${file}" />
    
    </#if>    

</body>
</html>