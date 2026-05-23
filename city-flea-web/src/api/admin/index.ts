import request from '@/utils/request'

export const adminAPI = {
  login: (data: { username: string; password: string }) => {
    return request.post('/admin/employee/login', data)
  },

  getWorkspaceData: () => {
    return request.get('/admin/workspace/businessData')
  },

  employee: {
    page: (params: { page?: number; pageSize?: number }) => {
      return request.get('/admin/employee/page', { params })
    },
    delete: (id: number) => {
      return request.delete(`/admin/employee/${id}`)
    },
    updateStatus: (id: number, data: { status: number }) => {
      return request.put(`/admin/employee/status/${id}`, data)
    }
  },

  category: {
    list: () => {
      return request.get('/admin/category/list')
    },
    add: (data: any) => {
      return request.post('/admin/category', data)
    },
    update: (data: any) => {
      return request.put('/admin/category', data)
    },
    delete: (id: number) => {
      return request.delete(`/admin/category/${id}`)
    }
  },

  goods: {
    page: (params: any) => {
      return request.get('/admin/goods/page', { params })
    },
    audit: (id: number, data: { status: number; rejectReason?: string }) => {
      return request.put(`/admin/goods/audit/${id}`, data)
    },
    detail: (id: number) => {
      return request.get(`/admin/goods/${id}`)
    }
  },

  order: {
    page: (params: any) => {
      return request.get('/admin/order/page', { params })
    },
    detail: (id: number) => {
      return request.get(`/admin/order/${id}`)
    },
    cancel: (id: number) => {
      return request.put(`/admin/order/cancel/${id}`)
    },
    refund: (id: number) => {
      return request.put(`/admin/order/refund/${id}`)
    }
  },

  freight: {
    list: () => {
      return request.get('/admin/freight/list')
    },
    enabled: () => {
      return request.get('/admin/freight/enabled')
    },
    add: (data: any) => {
      return request.post('/admin/freight', data)
    },
    update: (data: any) => {
      return request.put('/admin/freight', data)
    },
    delete: (id: number) => {
      return request.delete(`/admin/freight/${id}`)
    }
  },

  express: {
    list: () => {
      return request.get('/admin/express/list')
    },
    add: (data: any) => {
      return request.post('/admin/express', data)
    },
    update: (data: any) => {
      return request.put('/admin/express', data)
    },
    delete: (id: number) => {
      return request.delete(`/admin/express/${id}`)
    }
  },

  report: {
    turnover: (params: any) => {
      return request.get('/admin/report/turnover', { params })
    },
    order: (params: any) => {
      return request.get('/admin/report/order', { params })
    },
    user: (params: any) => {
      return request.get('/admin/report/user', { params })
    },
    sales: (params: any) => {
      return request.get('/admin/report/top10', { params })
    }
  }
}
