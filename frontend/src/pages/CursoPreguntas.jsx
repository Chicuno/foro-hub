import { useState, useEffect } from 'react'
import { useParams, Link } from 'react-router-dom'
import { cursosAPI } from '../services/api'
import Button from '../components/ui/Button'
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '../components/ui/Card'
import Badge from '../components/ui/Badge'
import { MessageSquare, Clock, ChevronRight, ArrowLeft, PlusCircle } from 'lucide-react'

const CursoPreguntas = () => {
  const { cursoId } = useParams()
  const [cursoNombre, setCursoNombre] = useState('')
  const [preguntas, setPreguntas] = useState([])
  const [loading, setLoading] = useState(true)
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)

  useEffect(() => {
    loadCurso()
  }, [cursoId])

  useEffect(() => {
    loadPreguntas()
  }, [cursoId, page])

  const loadCurso = async () => {
    try {
      const curso = await cursosAPI.obtener(cursoId)
      setCursoNombre(curso.nombre)
    } catch (error) {
      console.error('Error al cargar datos del curso:', error)
    }
  }

  const loadPreguntas = async () => {
    setLoading(true)
    try {
      const data = await cursosAPI.obtenerPreguntas(cursoId, page, 10)
      setPreguntas(data.content)
      setTotalPages(data.totalPages)
    } catch (error) {
      console.error('Error al cargar preguntas del curso:', error)
    } finally {
      setLoading(false)
    }
  }

  const getStatusColor = (status) => {
    switch (status) {
      case 'RESUELTA':
        return 'bg-green-500'
      case 'RESPONDIDA':
        return 'bg-blue-500'
      case 'SIN_RESPUESTA':
        return 'bg-yellow-500'
      case 'SPAM':
        return 'bg-red-500'
      default:
        return 'bg-gray-500'
    }
  }

  const formatDate = (dateString) => {
    const date = new Date(dateString)
    return date.toLocaleDateString('es-ES', {
      year: 'numeric',
      month: 'long',
      day: 'numeric',
      hour: '2-digit',
      minute: '2-digit'
    })
  }

  return (
    <div className="space-y-6">
      <div className="flex items-center space-x-4">
        <Link to="/cursos">
          <Button variant="ghost" size="sm">
            <ArrowLeft className="h-4 w-4 mr-2" />
            Volver a Cursos
          </Button>
        </Link>
        <h1 className="text-3xl font-bold">Preguntas del Curso {cursoNombre || cursoId}</h1>
        <Link to={`/cursos/${cursoId}/preguntas/nueva`}>
          <Button variant="outline" size="sm">
            <PlusCircle className="h-4 w-4 mr-2" />
            Nueva Pregunta
          </Button>
        </Link>
      </div>

      {loading ? (
        <div className="text-center py-12">
          <div className="text-muted-foreground">Cargando preguntas...</div>
        </div>
      ) : preguntas.length === 0 ? (
        <Card>
          <CardContent className="py-12 text-center">
            <MessageSquare className="h-12 w-12 mx-auto mb-4 text-muted-foreground" />
            <p className="text-muted-foreground">Este curso no tiene preguntas aún</p>
          </CardContent>
        </Card>
      ) : (
        <div className="space-y-4">
          {preguntas.map((pregunta) => (
            <Link key={pregunta.id} to={`/pregunta/${pregunta.id}`}>
              <Card className="hover:shadow-md transition-shadow cursor-pointer">
                <CardHeader>
                  <div className="flex items-start justify-between">
                    <div className="flex-1">
                      <CardTitle className="text-xl mb-2">{pregunta.titulo}</CardTitle>
                      <CardDescription className="line-clamp-2">
                        {pregunta.mensaje}
                      </CardDescription>
                    </div>
                    <ChevronRight className="h-5 w-5 text-muted-foreground ml-4 mt-1" />
                  </div>
                </CardHeader>
                <CardContent>
                  <div className="flex items-center justify-between text-sm text-muted-foreground">
                    <div className="flex items-center space-x-4">
                      <div className="flex items-center space-x-1">
                        <Clock className="h-4 w-4" />
                        <span>{formatDate(pregunta.fechaCreacion)}</span>
                      </div>
                      <div className="flex items-center space-x-1">
                        <MessageSquare className="h-4 w-4" />
                        <span>{pregunta.numeroRespuestas} respuestas</span>
                      </div>
                    </div>
                    <Badge className={getStatusColor(pregunta.status)}>
                      {pregunta.status}
                    </Badge>
                  </div>
                </CardContent>
              </Card>
            </Link>
          ))}
        </div>
      )}

      {totalPages > 1 && (
        <div className="flex justify-center space-x-2">
          <Button
            variant="outline"
            onClick={() => setPage(p => Math.max(0, p - 1))}
            disabled={page === 0}
          >
            Anterior
          </Button>
          <span className="flex items-center px-4">
            Página {page + 1} de {totalPages}
          </span>
          <Button
            variant="outline"
            onClick={() => setPage(p => Math.min(totalPages - 1, p + 1))}
            disabled={page === totalPages - 1}
          >
            Siguiente
          </Button>
        </div>
      )}
    </div>
  )
}

export default CursoPreguntas
