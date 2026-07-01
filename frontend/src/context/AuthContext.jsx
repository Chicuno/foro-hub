import { createContext, useContext, useState, useEffect } from 'react'
import { authAPI } from '../services/api'

const AuthContext = createContext(null)

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const token = localStorage.getItem('token')
    const userData = localStorage.getItem('user')
    if (token) {
      try {
        const payload = JSON.parse(atob(token.split('.')[1]))
        const exp = payload.exp * 1000 // Convertir a milisegundos
        if (Date.now() < exp) {
          const parsedUser = userData ? JSON.parse(userData) : {}
          setUser({
            nombreUsuario: parsedUser.nombreUsuario || payload.sub,
            id: parsedUser.id || payload.userId
          })
        } else {
          localStorage.removeItem('token')
          localStorage.removeItem('user')
        }
      } catch (error) {
        localStorage.removeItem('token')
        localStorage.removeItem('user')
      }
    }
    setLoading(false)
  }, [])

  const login = async (nombreUsuario, contrasena) => {
    try {
      const data = await authAPI.login(nombreUsuario, contrasena)
      const payload = JSON.parse(atob(data.token.split('.')[1]))
      const user = { nombreUsuario, id: payload.userId }
      localStorage.setItem('token', data.token)
      localStorage.setItem('user', JSON.stringify(user))
      setUser(user)
      return data
    } catch (error) {
      localStorage.removeItem('token')
      localStorage.removeItem('user')
      setUser(null)
      throw error
    }
  }

  const logout = () => {
    localStorage.removeItem('token')
    localStorage.removeItem('user')
    setUser(null)
  }

  return (
    <AuthContext.Provider value={{ user, login, logout, loading }}>
      {children}
    </AuthContext.Provider>
  )
}

export const useAuth = () => {
  const context = useContext(AuthContext)
  if (!context) {
    throw new Error('useAuth must be used within an AuthProvider')
  }
  return context
}
