<template>
  <div>
    <Row>
      <Form ref="formData" :model="formData" :label-width="80">
        <Row style="margin-top: 10px;">
          <Col span="8">
            <FormItem label="名称" prop="sname">
              <Input v-model="formData.sname" placeholder="名称"></Input>
            </FormItem>
          </Col>
          <Col span="8">
            <FormItem label="住址" prop="address">
              <Input v-model="formData.address" placeholder="住址"></Input>
            </FormItem>
          </Col>
          <Col span="8">
            <Divider type="vertical"/>
            <Button type="primary" @click="add">添加</Button>
            <Button type="primary" @click="removeBatch" style="margin-left: 8px">删除</Button>
            <Button type="primary" @click="query" style="margin-left: 8px">查询</Button>
          </Col>
        </Row>
      </Form>
    </Row>

    <div>
      <Table stripe ref="selection" :columns="columns" :data="rows"></Table>
    </div>
    <div class="paging">
      <Page :total="total" :page-size="pageSize" show-sizer show-elevator show-total
            @on-change="changePage" @on-page-size-change="changePageSize"></Page>
    </div>
  </div>
</template>
<style scoped>
.paging {
  float: right;
  margin-top: 10px;
}
</style>
<script>
import { baseList } from '@/libs/crud/base-list'

export default {
  mixins: [baseList],
  data () {
    return {
      formData: {
        sname: '',
        address: ''
      },
      columns: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        {
          title: '名称',
          key: 'sname'
        },
        {
          title: '电话',
          key: 'consphone'
        },
        {
          title: '住址',
          key: 'address'
        },
        {
          title: '操作',
          key: 'action',
          width: 150,
          align: 'center',
          render: (h, params) => {
            return h('div', [
              h('Button', {
                props: {
                  type: 'primary',
                  size: 'small'
                },
                style: {
                  marginRight: '5px'
                },
                on: {
                  click: () => {
                    this.edit(params.row.id)
                  }
                }
              }, '修改'),
              h('Button', {
                props: {
                  type: 'primary',
                  size: 'small'
                },
                on: {
                  click: () => {
                    this.remove(params.row.id, params.index)
                  }
                }
              }, '删除')
            ])
          }
        }
      ]
    }
  }
}
</script>
