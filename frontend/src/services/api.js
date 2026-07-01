import axios from 'axios'

const api = axios.create({
  headers: {
    'Content-Type': 'application/json',
  },
})

// Interceptor para agregar el token JWT a las requests
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    // No enviar token en el endpoint de login
    if (token && !config.url.includes('/login')) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Interceptor para manejar errores de autenticación
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401 && !error.config?.url?.includes('/login')) {
      localStorage.removeItem('token')
      window.location.href = '/auth/login'
    }
    return Promise.reject(error)
  }
)

export const authAPI = {
  login: async (nombreUsuario, contrasena) => {
    const response = await api.post('/login', { nombreUsuario, contrasena })
    return response.data
  },
}

export const preguntasAPI = {
  listar: async (page = 0, size = 10, sortDirection = 'desc') => {
    const response = await api.get('/preguntas', {
      params: { page, size, sort: `fechaCreacion,${sortDirection}` }
    })
    return response.data
  },
  listarAntiguosPrimero: async (page = 0, size = 10) => {
    const response = await api.get('/preguntas/antiguos-primero', {
      params: { page, size, sort: 'fechaCreacion,asc' }
    })
    return response.data
  },
  listarPorStatus: async (status, page = 0, size = 10, sortDirection = 'desc') => {
    const response = await api.get(`/preguntas/por-status/${status}`, {
      params: { page, size, sort: `fechaCreacion,${sortDirection}` }
    })
    return response.data
  },
  obtener: async (id) => {
    const response = await api.get(`/preguntas/${id}`)
    return response.data
  },
  crear: async (datos) => {
    const response = await api.post('/preguntas', datos)
    return response.data
  },
  actualizar: async (datos) => {
    const response = await api.put('/preguntas', datos)
    return response.data
  },
  eliminar: async (id) => {
    await api.delete(`/preguntas/${id}`)
  },
}

export const respuestasAPI = {
  listar: async (page = 0, size = 10) => {
    const response = await api.get('/respuestas', {
      params: { page, size, sort: 'fechaCreacion,desc' }
    })
    return response.data
  },
  obtener: async (id) => {
    const response = await api.get(`/respuestas/${id}`)
    return response.data
  },
  listarPorPregunta: async (preguntaId, page = 0, size = 10) => {
    const response = await api.get(`/respuestas/pregunta/${preguntaId}`, {
      params: { page, size, sort: 'fechaCreacion,asc' }
    })
    return response.data
  },
  crear: async (datos) => {
    const response = await api.post('/respuestas', datos)
    return response.data
  },
  actualizar: async (datos) => {
    const response = await api.put('/respuestas', datos)
    return response.data
  },
}

export const usuariosAPI = {
  registrar: async (datos) => {
    const response = await api.post('/usuarios', datos)
    return response.data
  },
  listar: async (page = 0, size = 10) => {
    const response = await api.get('/usuarios', {
      params: { page, size, sort: 'nombre,asc' }
    })
    return response.data
  },
  obtenerPreguntas: async (usuarioId, page = 0, size = 10) => {
    const response = await api.get(`/usuarios/${usuarioId}/preguntas`, {
      params: { page, size, sort: 'fechaCreacion,desc' }
    })
    return response.data
  },
  eliminar: async (id) => {
    await api.delete(`/usuarios/${id}`)
  },
}

export const cursosAPI = {
  listar: async (page = 0, size = 10) => {
    const response = await api.get('/cursos', {
      params: { page, size, sort: 'nombre,asc' }
    })
    return response.data
  },
  obtener: async (id) => {
    const response = await api.get(`/cursos/${id}`)
    return response.data
  },
  obtenerPreguntas: async (cursoId, page = 0, size = 10) => {
    const response = await api.get(`/cursos/${cursoId}/preguntas`, {
      params: { page, size, sort: 'fechaCreacion,desc' }
    })
    return response.data
  },
}

export default api
