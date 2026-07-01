import { createContext, useContext, useState, useEffect } from 'react'
import { authAPI, usuariosAPI } from '../services/api'

const AuthContext = createContext(null)

export const AuthProvider = ({ children }) => {
  const [user, setUser] = useState(null)
  const [loading, setLoading] = useState(true)

  useEffect(() => {
    const loadUser = async () => {
      const token = localStorage.getItem('token')
      const userData = localStorage.getItem('user')

      if (token) {
        try {
          const payload = JSON.parse(atob(token.split('.')[1]))
          const exp = payload.exp * 1000

          if (Date.now() < exp) {
            const parsedUser = userData ? JSON.parse(userData) : {}
            const username = parsedUser.nombreUsuario || payload.sub
            const userId = parsedUser.id || payload.userId
            let nombre = parsedUser.nombre || username

            try {
              const response = await usuariosAPI.listar(0, 100)
              const usuarioEncontrado = response.content?.find(
                (usuario) => usuario.nombreUsuario === username
              )

              if (usuarioEncontrado?.nombre) {
                nombre = usuarioEncontrado.nombre
              }
            } catch (error) {
              console.error('Error al cargar el nombre del usuario:', error)
            }

            const userProfile = {
              nombreUsuario: username,
              nombre,
              id: userId
            }

            localStorage.setItem('user', JSON.stringify(userProfile))
            setUser(userProfile)
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
    }

    loadUser()
  }, [])

  const login = async (nombreUsuario, contrasena) => {
    try {
      const data = await authAPI.login(nombreUsuario, contrasena)
      const payload = JSON.parse(atob(data.token.split('.')[1]))
      let nombre = nombreUsuario

      try {
        const response = await usuariosAPI.listar(0, 100)
        const usuarioEncontrado = response.content?.find(
          (usuario) => usuario.nombreUsuario === nombreUsuario
        )

        if (usuarioEncontrado?.nombre) {
          nombre = usuarioEncontrado.nombre
        }
      } catch (error) {
        console.error('Error al cargar el nombre del usuario:', error)
      }

      const user = { nombreUsuario, nombre, id: payload.userId }
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
