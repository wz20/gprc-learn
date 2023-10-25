package com.wz.client;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.wz.TestProto;
import com.wz.TestServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName: GrpcClientFutureStub
 * @Description: FutureStub
 * @Author: Ze WANG
 * @Date: 2023/10/13
 * @Email: Ze_Wang2@human-horizons.com
 * @Version 1.0
 **/
public class GrpcClientFutureStub {
    public static void main(String[] args) {
        //1.创建通信的管道
        ManagedChannel managedChannel = ManagedChannelBuilder.forAddress("localhost",9000).usePlaintext().build();
        try {
            TestServiceGrpc.TestServiceFutureStub testServiceFutureStub = TestServiceGrpc.newFutureStub(managedChannel);
            ListenableFuture<TestProto.TestResponse> future = testServiceFutureStub.testWz(TestProto.TestRequest.newBuilder().setName("wangze").build());
            //同步
//            TestProto.TestResponse testResponse = future.get();
//            System.out.println(testResponse);
//            System.out.println("后续操作...........");
            //异步
            Futures.addCallback(future, new FutureCallback<TestProto.TestResponse>() {
                @Override
                public void onSuccess(TestProto.TestResponse result) {
                    //成功的情况
                    System.out.println(result);
                }

                @Override
                public void onFailure(Throwable t) {
                    //异常错误
                    System.out.println(t.getMessage());
                }
            }, Executors.newCachedThreadPool());

            System.out.println("后续操作.......");
            managedChannel.awaitTermination(20, TimeUnit.SECONDS);

        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            managedChannel.shutdown();
        }
    }
}
