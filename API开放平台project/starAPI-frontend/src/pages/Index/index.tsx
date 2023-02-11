import { PageContainer } from '@ant-design/pro-components';
import {Avatar, Card, List, message, Skeleton, theme} from 'antd';
import React, {useEffect, useState} from 'react';
import {listInterfaceInfoByPageUsingGET} from "@/services/starAPI-backend/interfaceInfoController";


const Index: React.FC = () => {
  const [loading, setLoading] = useState(false);
  const [list, setList] = useState<API.InterfaceInfo[]>([]);
  const [total, setTotal] = useState<number>(0)

  const loadData = async (current = 1, pageSize = 10) => {
    setLoading(true)
    try {
      const res = await listInterfaceInfoByPageUsingGET({

      });
      setList(res?.data?.records ?? []);
      setTotal(res?.data?.total ?? 0);
    }
    catch (error : any) {
      message.error("request fail " + error.message)
    }
    setLoading(false)
  }

  useEffect(() => {
    loadData();
  }, [])


  return (
    <PageContainer title="star API open source platform">
      <List
        className="Interface List"
        loading={loading}
        itemLayout="horizontal"
        dataSource={list}
        renderItem={(item) => {
          const apiLink = `/interface_info/${item.id}`;
          return <List.Item
            actions={[<a key={item.id} href={apiLink}>edit</a>]}
          >
              <List.Item.Meta
                title={<a href={apiLink}>{item.name}</a>}
                description={item.description}
              />
          </List.Item>
        }}
        pagination={
          {
            showTotal(total){
              return "total number: " + total;
            },
            pageSize: 10,
            total,
            onChange(page, pageSize){
              loadData(page, pageSize);
            }
          }
        }
      />
    </PageContainer>
  );
};

export default Index;
