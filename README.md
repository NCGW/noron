#### 程序运行说明

##### 方案一 源码运行（需要安装scala和sbt环境）

- 下载Release v2020.05.21中的source code的压缩包，或者克隆仓库

- 解压后，将backend/src/main/resources/application.conf 文件中将数据库地址改为数据库文件data的本机位置

- 命令行进入到解压后的文件路径，并输入以下命令

  ```
  sbt
  reload
  project backend
  reStart
  ```

- 在浏览器中输入http://localhost:30388/noron，打开开发者模式选中手机模式开始预览

##### 方案二 可执行文件运行

- 下载Release v2020.05.21中的backend-2020.05.21.tar.gz和数据库文件data.zip
- 解压下载好的两个文件，并将data放在backend-2020.05.21/下
- 下载application.conf，将数据库地址改为数据库文件data的本机位置，并重命名为product.conf，放置于与backend-2020.05.21/下
- 命令行运行可执行程序
- 在浏览器中输入http://localhost:30388/noron，打开开发者模式选中手机模式开始预览

##### 补充说明

- application.conf修改方式

  找到文件中slick.db字段

  ```
  slick.db{
  	//修改url为本机data文件地址，最好使用绝对路径
  	url = "jdbc:h2:file:{data文件的绝对路径或者相对路径}"
  	// 其余字段不做改变
  }
  ```

  
