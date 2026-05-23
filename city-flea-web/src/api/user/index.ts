import request from '@/utils/request'

export const userAPI = {
  login: (data: { code: string }) => {
    return request.post('/user/user/login', data)
  },

  userInfo: () => {
    return request.get('/user/user/info')
  },

  category: {
    list: () => {
      return request.get('/user/category/list')
    }
  },

  goods: {
    list: (params: any) => {
      return request.get('/user/goods/list', { params })
    },
    detail: (id: number) => {
      return request.get(`/user/goods/${id}`)
    },
    recommend: () => {
      return request.get('/user/goods/recommend')
    }
  },

  cart: {
    list: () => {
      return request.get('/user/cart/list')
    },
    add: (data: { goodsId: number; quantity: number }) => {
      return request.post('/user/cart/add', data)
    },
    update: (data: { goodsId: number; quantity: number }) => {
      return request.put('/user/cart/update', data)
    },
    remove: (goodsId: number) => {
      return request.delete(`/user/cart/remove/${goodsId}`)
    },
    clear: () => {
      return request.delete('/user/cart/clear')
    }
  },

  address: {
    list: () => {
      return request.get('/user/addressBook/list')
    },
    default: () => {
      return request.get('/user/addressBook/default')
    },
    setDefault: (id: number) => {
      return request.put(`/user/addressBook/default/${id}`)
    },
    add: (data: any) => {
      return request.post('/user/addressBook', data)
    },
    update: (data: any) => {
      return request.put('/user/addressBook', data)
    },
    delete: (id: number) => {
      return request.delete(`/user/addressBook/${id}`)
    }
  },

  order: {
    submit: (data: any) => {
      return request.post('/user/order/submit', data)
    },
    payment: (data: { orderNumber: string; payMethod: number }) => {
      return request.post('/user/order/payment', data)
    },
    history: (params: any) => {
      return request.get('/user/order/historyOrders', { params })
    },
    detail: (id: number) => {
      return request.get(`/user/order/${id}`)
    },
    cancel: (id: number) => {
      return request.put(`/user/order/cancel/${id}`)
    },
    receive: (id: number) => {
      return request.put(`/user/order/receive/${id}`)
    }
  },

  express: {
    info: (orderId: number) => {
      return request.get(`/user/express/${orderId}`)
    },
    track: (orderId: number) => {
      return request.get(`/user/express/track/${orderId}`)
    }
  },

  seller: {
    goods: {
      list: (params: any) => {
        return request.get('/user/seller/goods/list', { params })
      },
      add: (data: any) => {
        return request.post('/user/seller/goods', data)
      },
      update: (data: any) => {
        return request.put('/user/seller/goods', data)
      },
      delete: (id: number) => {
        return request.delete(`/user/seller/goods/${id}`)
      },
      status: (id: number, data: { status: number }) => {
        return request.put(`/user/seller/goods/status/${id}`, data)
      }
    },
    order: {
      list: (params: any) => {
        return request.get('/user/seller/order/list', { params })
      },
      detail: (id: number) => {
        return request.get(`/user/seller/order/${id}`)
      },
      ship: (data: { orderId: number; expressCode: string; trackingNumber: string }) => {
        return request.post('/user/seller/order/ship', data)
      },
      expressCompanies: () => {
        return request.get('/user/seller/order/express/companies')
      }
    },
    account: {
      info: () => {
        return request.get('/user/seller/account/info')
      },
      flow: (params: any) => {
        return request.get('/user/seller/account/flow', { params })
      }
    }
  }
}
