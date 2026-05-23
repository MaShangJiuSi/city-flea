import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export interface CartItem {
  id?: number
  goodsId: number
  goodsName: string
  image?: string
  price: number
  quantity: number
}

export const useCartStore = defineStore('cart', () => {
  const items = ref<CartItem[]>(
    localStorage.getItem('cart')
      ? JSON.parse(localStorage.getItem('cart')!)
      : []
  )

  const totalCount = computed(() => {
    return items.value.reduce((sum, item) => sum + item.quantity, 0)
  })

  const totalPrice = computed(() => {
    return items.value.reduce((sum, item) => sum + item.price * item.quantity, 0)
  })

  const addItem = (item: CartItem) => {
    const existingItem = items.value.find(
      (i) => i.goodsId === item.goodsId
    )
    if (existingItem) {
      existingItem.quantity += item.quantity
    } else {
      items.value.push(item)
    }
    saveToStorage()
  }

  const removeItem = (goodsId: number) => {
    const index = items.value.findIndex((i) => i.goodsId === goodsId)
    if (index !== -1) {
      items.value.splice(index, 1)
      saveToStorage()
    }
  }

  const updateQuantity = (goodsId: number, quantity: number) => {
    const item = items.value.find((i) => i.goodsId === goodsId)
    if (item) {
      if (quantity <= 0) {
        removeItem(goodsId)
      } else {
        item.quantity = quantity
        saveToStorage()
      }
    }
  }

  const clearCart = () => {
    items.value = []
    saveToStorage()
  }

  const saveToStorage = () => {
    localStorage.setItem('cart', JSON.stringify(items.value))
  }

  return {
    items,
    totalCount,
    totalPrice,
    addItem,
    removeItem,
    updateQuantity,
    clearCart
  }
})
