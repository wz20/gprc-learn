package com.wz.service;

import com.wz.HelloProto;
import com.wz.HelloServiceGrpc;
import io.grpc.stub.StreamObserver;

/**
 * @ClassName: HelloServiceImpl
 * @Description: 具体得逻辑
 * @Author: Ze WANG
 * @Date: 2023/10/9
 * @Version 1.0
 **/
public class HelloServiceImpl extends HelloServiceGrpc.HelloServiceImplBase {
    /**
     * grpc 的返回值并不是通过Java中的返回值来返回。而是通过观察者设计模式通过参数返回的，后续会有详细的解释
     * 1. 接收客户端提交的参数
     * 2. 业务处理
     * 3. 返回处理结果
     * @param request req
     * @param responseObserver 响应观察者
     */
    @Override
    public void hello(HelloProto.HelloRequest request, StreamObserver<HelloProto.HelloResponse> responseObserver) {
        //1.接收client的请求参数
        String name = request.getName();
        //2.业务处理
        System.out.println("service name===>"+name);
        //3.封装响应
        //3.1 创建响应对象的构造者
        HelloProto.HelloResponse.Builder builder = HelloProto.HelloResponse.newBuilder();
        //3.2 填充数据
        builder.setResult("hello method invoke ok");
        //3.3 封装响应对象
        HelloProto.HelloResponse helloResponse = builder.build();
        //4.返回给客户端
        responseObserver.onNext(helloResponse);
        //5.通知客户端响应已结束
        responseObserver.onCompleted();
    }

    @Override
    public void c2ss(HelloProto.HelloRequest request, StreamObserver<HelloProto.HelloResponse> responseObserver) {
        //1.接收client的请求参数
        String name = request.getName();
        //2.业务处理
        System.out.println("service param name===>"+name);
        //3.封装响应
        for (int i = 0; i < 10; i++) {
            try {
                //3.1 创建响应对象的构造者
                HelloProto.HelloResponse.Builder builder = HelloProto.HelloResponse.newBuilder();
                //3.2 填充数据
                builder.setResult("Result==>" + i);
                //3.3 封装响应对象
                HelloProto.HelloResponse helloResponse = builder.build();
                //4.返回给客户端
                responseObserver.onNext(helloResponse);
                //模拟时间间隔
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        //5.通知客户端响应已结束
        responseObserver.onCompleted();
    }

    /**
     * 客户端流式RPC服务端
     * @param responseObserver 返回responseObserver
     * @return 监控请求消息 StreamObserver
     */
    @Override
    public StreamObserver<HelloProto.HelloRequest> cs2s(StreamObserver<HelloProto.HelloResponse> responseObserver) {
        return new StreamObserver<HelloProto.HelloRequest>() {
            @Override
            public void onNext(HelloProto.HelloRequest value) {
                //1.监控每一个client发送的消息
                System.out.println("接收到客户端消息："+value);
            }

            @Override
            public void onError(Throwable t) {
                //2.监控异常
                System.out.println("监听到异常："+t.getMessage());
            }

            @Override
            public void onCompleted() {
                //3.全部消息是否发送完整
                System.out.println("客户端消息全部接收成功");
                //响应一个全部信息
                HelloProto.HelloResponse.Builder builder = HelloProto.HelloResponse.newBuilder();
                builder.setResult("all message is ok");
                HelloProto.HelloResponse helloResponse = builder.build();
                responseObserver.onNext(helloResponse);
                responseObserver.onCompleted();
            }
        };
    }

    /**
     * 双向流RPC的服务端
     * @param responseObserver 响应观察者
     * @return StreamObserver
     */
    @Override
    public StreamObserver<HelloProto.HelloRequest> cs2ss(StreamObserver<HelloProto.HelloResponse> responseObserver) {
        return new StreamObserver<HelloProto.HelloRequest>() {
            @Override
            public void onNext(HelloProto.HelloRequest value) {
                //1.监控每一个client发送的消息
                System.out.println("接收到客户端消息："+value);
                //每次都返回
                HelloProto.HelloResponse.Builder builder = HelloProto.HelloResponse.newBuilder();
                builder.setResult("message is ok");
                HelloProto.HelloResponse helloResponse = builder.build();
                responseObserver.onNext(helloResponse);
            }

            @Override
            public void onError(Throwable t) {
                //2.监控异常
                System.out.println("监听到异常："+t.getMessage());
            }

            @Override
            public void onCompleted() {
                //3.全部消息是否发送完整
                System.out.println("客户端消息全部接收成功");
                //响应一个全部信息
                HelloProto.HelloResponse.Builder builder = HelloProto.HelloResponse.newBuilder();
                builder.setResult("all message is ok");
                HelloProto.HelloResponse helloResponse = builder.build();
                responseObserver.onNext(helloResponse);
                responseObserver.onCompleted();
            }
        };
    }
}
