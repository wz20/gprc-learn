package com.wz.service;

import com.wz.TestProto;
import com.wz.TestServiceGrpc;
import io.grpc.stub.StreamObserver;

/**
 * @ClassName: TestServiceImpl
 * @Description: TestServiceImpl
 * @Author: Ze WANG
 * @Date: 2023/10/13
 * @Email: Ze_Wang2@human-horizons.com
 * @Version 1.0
 **/
public class TestServiceImpl extends TestServiceGrpc.TestServiceImplBase {
    @Override
    public void testWz(TestProto.TestRequest request, StreamObserver<TestProto.TestResponse> responseObserver) {
        String name = request.getName();
        System.out.println("name:"+name);
        //响应
        responseObserver.onNext(TestProto.TestResponse.newBuilder().setResult("test is ok").build());
        responseObserver.onCompleted();
    }
}
