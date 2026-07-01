import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom'
import { AuthProvider, useAuth } from './context/AuthContext'
import Layout from './components/Layout'
import Login from './pages/Login'
import Register from './pages/Register'
import Home from './pages/Home'
import PreguntaDetail from './pages/PreguntaDetail'
import NuevaPregunta from './pages/NuevaPregunta'
import Cursos from './pages/Cursos'
import Usuarios from './pages/Usuarios'
import UsuarioPreguntas from './pages/UsuarioPreguntas'
import CursoPreguntas from './pages/CursoPreguntas'

const ProtectedRoute = ({ children }) => {
  const { user, loading } = useAuth()
  
  if (loading) {
    return <div className="min-h-screen flex items-center justify-center">Cargando...</div>
  }
  
  if (!user) {
    return <Navigate to="/auth/login" replace />
  }
  
  return children
}

function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <Routes>
          <Route path="/auth/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/" element={
            <ProtectedRoute>
              <Layout />
            </ProtectedRoute>
          }>
            <Route index element={<Home />} />
            <Route path="pregunta/:id" element={<PreguntaDetail />} />
            <Route path="nueva-pregunta" element={<NuevaPregunta />} />
            <Route path="cursos" element={<Cursos />} />
            <Route path="cursos/:cursoId/preguntas" element={<CursoPreguntas />} />
            <Route path="cursos/:cursoId/preguntas/nueva" element={<NuevaPregunta />} />
            <Route path="usuarios" element={<Usuarios />} />
            <Route path="usuarios/:usuarioId/preguntas" element={<UsuarioPreguntas />} />
          </Route>
        </Routes>
      </BrowserRouter>
    </AuthProvider>
  )
}

export default App
