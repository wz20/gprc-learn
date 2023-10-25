package com.wz.client;

import com.wz.HelloProto;
import com.wz.HelloServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.ClientResponseObserver;
import io.grpc.stub.StreamObserver;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: GrpcClient3
 * @Description: 异步
 * @Author: Ze WANG
 * @Date: 2023/10/13
 * @Email: Ze_Wang2@human-horizons.com
 * @Version 1.0
 **/
public class GrpcClient3 {
    /**
     * 用于存储响应消息
     */
    static List<HelloProto.HelloResponse> responses = new ArrayList<>();

    public static void main(String[] args) {
        //1.创建通信的管道
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost",9000).usePlaintext().build();


        try {
            //2.获得代理对象 stub
            HelloServiceGrpc.HelloServiceStub helloServiceStub = HelloServiceGrpc.newStub(managedChannel);
            //3.完成rpc调用
            //3.1 准备参数
            HelloProto.HelloRequest.Builder builder = HelloProto.HelloRequest.newBuilder();
            builder.setName("WangZe");
            HelloProto.HelloRequest helloRequest = builder.build();
            //3.2 rpc请求
           helloServiceStub.c2ss(helloRequest, new StreamObserver<HelloProto.HelloResponse>() {
               //3.3 响应的监听，根据不同的时间做不同的处理 onNext() onError() onCompleted()
               @Override
                public void onNext(HelloProto.HelloResponse value) {
                    //监听消息
                   System.out.println("服务端发来了一个消息："+value);
                   responses.add(value);
                }

                @Override
                public void onError(Throwable t) {
                    //监听异常
                    System.out.println("ERROR:"+t.getMessage());
                }

                @Override
                public void onCompleted() {
                    //把服务端所有的数据拿到后，再进行业务处理，在此处写
                    System.out.println("所有消息集合：");
                    responses.forEach(System.out::println);
                }
            });
           //设置等待时间
            managedChannel.awaitTermination(60, TimeUnit.SECONDS);

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            managedChannel.shutdown();
            System.out.println("client处理结束......");
        }
    }
}
