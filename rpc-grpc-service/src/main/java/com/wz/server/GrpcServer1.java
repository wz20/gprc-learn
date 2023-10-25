package com.wz.server;

import com.wz.service.HelloServiceImpl;
import com.wz.service.TestServiceImpl;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * @ClassName: GrpcServer1
 * @Description: 服务端1
 * @Author: Ze WANG
 * @Date: 2023/10/9
 * @Version 1.0
 **/
public class GrpcServer1 {
    public static void main(String[] args) throws InterruptedException, IOException {
        //1.设置端口
        ServerBuilder<?> serverBuilder = ServerBuilder.forPort(9000);
        //2.发布服务
        serverBuilder.addService(new HelloServiceImpl());
        serverBuilder.addService(new TestServiceImpl());
        //3.创建服务对象
        Server server = serverBuilder.build();
        //4.启动服务器
        server.start();
        server.awaitTermination();
    }
}
