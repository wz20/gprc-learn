package com.wz.client;

import com.wz.HelloProto;
import com.wz.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

/**
 * @ClassName: GrpcClient1
 * @Description: 客户端
 * @Author: Ze WANG
 * @Date: 2023/10/9
 * @Version 1.0
 **/
public class GrpcClient1 {
    public static void main(String[] args) {
        //1.创建通信的管道
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost",9000).usePlaintext().build();
        try {
            //2.获得代理对象 stub
            HelloServiceGrpc.HelloServiceBlockingStub helloServiceBlockingStub = HelloServiceGrpc.newBlockingStub(managedChannel);
            //3.完成rpc调用
            //3.1 准备参数
            HelloProto.HelloRequest.Builder builder = HelloProto.HelloRequest.newBuilder();
            builder.setName("WangZe");
            HelloProto.HelloRequest helloRequest = builder.build();
            //3.2 rpc请求
            HelloProto.HelloResponse helloResponse = helloServiceBlockingStub.hello(helloRequest);
            //3.3 获取返回值
            String result = helloResponse.getResult();
            System.out.println("rpc响应内容==》"+result);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            managedChannel.shutdown();
        }
    }
}
