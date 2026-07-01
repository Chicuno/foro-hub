import { useState, useEffect } from 'react'
import { useNavigate, useParams } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'
import { preguntasAPI, cursosAPI } from '../services/api'
import Button from '../components/ui/Button'
import Input from '../components/ui/Input'
import Textarea from '../components/ui/Textarea'
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '../components/ui/Card'
import { PlusCircle } from 'lucide-react'

const NuevaPregunta = () => {
  const { cursoId: cursoIdParam } = useParams()
  const { user } = useAuth()
  const [titulo, setTitulo] = useState('')
  const [mensaje, setMensaje] = useState('')
  const [cursoId, setCursoId] = useState(cursoIdParam || '')
  const [cursos, setCursos] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState('')
  const navigate = useNavigate()

  useEffect(() => {
    if (!cursoIdParam) {
      loadCursos()
    }
  }, [cursoIdParam])

  const loadCursos = async () => {
    try {
      const data = await cursosAPI.listar(0, 100)
      setCursos(data.content)
    } catch (error) {
      console.error('Error al cargar cursos:', error)
    }
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)

    const selectedCursoId = cursoIdParam || cursoId

    try {
      await preguntasAPI.crear({
        titulo,
        mensaje,
        idCurso: selectedCursoId ? parseInt(selectedCursoId) : null,
        idUsuario: user?.id || null
      })
      navigate(`/cursos/${selectedCursoId}/preguntas`)
    } catch (err) {
      setError('Error al crear la pregunta. Intenta nuevamente.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="max-w-2xl mx-auto">
      <Card>
        <CardHeader>
          <div className="flex items-center space-x-2">
            <PlusCircle className="h-6 w-6 text-primary" />
            <CardTitle className="text-2xl">Nueva Pregunta</CardTitle>
          </div>
          <CardDescription>
            Comparte tu duda con la comunidad del foro
          </CardDescription>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmit} className="space-y-4">
            <div className="space-y-2">
              <label htmlFor="titulo" className="text-sm font-medium">
                Título *
              </label>
              <Input
                id="titulo"
                type="text"
                placeholder="Ej: ¿Cómo configurar Spring Security?"
                value={titulo}
                onChange={(e) => setTitulo(e.target.value)}
                required
                maxLength={200}
              />
            </div>

            {!cursoIdParam && (
              <div className="space-y-2">
                <label htmlFor="curso" className="text-sm font-medium">
                  Curso *
                </label>
                <select
                  id="curso"
                  value={cursoId}
                  onChange={(e) => setCursoId(e.target.value)}
                  required
                  className="flex h-10 w-full rounded-md border border-input bg-background px-3 py-2 text-sm ring-offset-background focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-ring"
                >
                  <option value="">Selecciona un curso</option>
                  {cursos.map((curso) => (
                    <option key={curso.id} value={curso.id}>
                      {curso.nombre}
                    </option>
                  ))}
                </select>
              </div>
            )}
            {cursoIdParam && (
              <div className="space-y-2">
                <label className="text-sm font-medium">Curso seleccionado</label>
                <Input value={`Curso ID: ${cursoIdParam}`} disabled />
              </div>
            )}

            <div className="space-y-2">
              <label htmlFor="mensaje" className="text-sm font-medium">
                Mensaje *
              </label>
              <Textarea
                id="mensaje"
                placeholder="Describe tu pregunta con el mayor detalle posible..."
                value={mensaje}
                onChange={(e) => setMensaje(e.target.value)}
                rows={8}
                required
                minLength={10}
              />
            </div>

            {error && (
              <div className="text-sm text-destructive bg-destructive/10 p-3 rounded-md">
                {error}
              </div>
            )}

            <div className="flex space-x-2">
              <Button type="submit" disabled={loading}>
                {loading ? 'Publicando...' : 'Publicar Pregunta'}
              </Button>
              <Button
                type="button"
                variant="outline"
                onClick={() => navigate('/')}
                disabled={loading}
              >
                Cancelar
              </Button>
            </div>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}

export default NuevaPregunta
