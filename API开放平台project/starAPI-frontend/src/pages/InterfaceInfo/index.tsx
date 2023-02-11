import {getInterfaceInfoByIdUsingGET, invokeInterfaceInfoUsingPOST,} from "@/services/starAPI-backend/interfaceInfoController";
import { PageContainer } from '@ant-design/pro-components';
import React, { useEffect, useState } from 'react';
import {Button, Card, Descriptions, Form, message, Input, Divider} from 'antd';
import { useParams } from '@@/exports';


const Index: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const [data, setData] = useState<API.InterfaceInfo>();
  const [invokeRes, setInvokeRes] = useState<any>();
  const [invokeLoading, setInvokeLoading] = useState(false);

  const params = useParams();

  const loadData = async () => {
    if (!params.id) {
      message.error('request params not exist');
      return;
    }
    setLoading(true);
    try {
      const res = await getInterfaceInfoByIdUsingGET({
        id: Number(params.id),
      });
      setData(res.data);
    } catch (error: any) {
      message.error('request fail，' + error.message);
    }
    setLoading(false);
  };

  const onFinish = async (values: any) => {
    if (!params.id) {
      message.error('interface not exist');
      return;
    }
    setInvokeLoading(true);
    try {
      const res = await invokeInterfaceInfoUsingPOST({
        id: params.id,
        ...values,
      });
      setInvokeRes(res.data);
      message.success('request success');
    } catch (error: any) {
      message.error('request fail，' + error.message);
    }
    setInvokeLoading(false);
  };

  useEffect(() => {
    loadData();
  }, []);

  return (
    <PageContainer title="">
      <Card>
        {data ? (
          <Descriptions title={data.name} column={1}>
            <Descriptions.Item label="interface status">
              {data.status ? 'online' : 'offline'}
            </Descriptions.Item>
            <Descriptions.Item label="description">{data.description}</Descriptions.Item>
            <Descriptions.Item label="request address">{data.url}</Descriptions.Item>
            <Descriptions.Item label="request method">{data.method}</Descriptions.Item>
            <Descriptions.Item label="request params">{data.requestParams}</Descriptions.Item>
            <Descriptions.Item label="request header">{data.requestHeader}</Descriptions.Item>
            <Descriptions.Item label="response header">{data.responseHeader}</Descriptions.Item>
            <Descriptions.Item label="create time">{data.createTime}</Descriptions.Item>
            <Descriptions.Item label="update time">{data.updateTime}</Descriptions.Item>
          </Descriptions>
        ) : (
          <>interface is not exist</>
        )}
      </Card>
      <Divider />
      <Card title="test online">
        <Form name="invoke" layout="vertical" onFinish={onFinish}>
          <Form.Item label="request params" name="userRequestParams">
            <Input.TextArea />
          </Form.Item>
          <Form.Item wrapperCol={{ span: 16 }}>
            <Button type="primary" htmlType="submit">
              调用
            </Button>
          </Form.Item>
        </Form>
      </Card>
      <Divider />
      <Card title="result" loading={invokeLoading}>
        {invokeRes}
      </Card>
    </PageContainer>
  );
};

export default Index;
