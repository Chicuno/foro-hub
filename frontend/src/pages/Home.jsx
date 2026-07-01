import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { preguntasAPI } from '../services/api'
import Button from '../components/ui/Button'
import Select from '../components/ui/Select'
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '../components/ui/Card'
import Badge from '../components/ui/Badge'
import { MessageSquare, Clock, User, ChevronRight } from 'lucide-react'

const Home = () => {
  const [preguntas, setPreguntas] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState(null)
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)
  const [filter, setFilter] = useState('todos')
  const [sortOrder, setSortOrder] = useState('recientes')

  useEffect(() => {
    loadPreguntas()
  }, [page, filter, sortOrder])

  const loadPreguntas = async () => {
    setLoading(true)
    setError(null)
    try {
      let data
      const sortDirection = sortOrder === 'recientes' ? 'desc' : 'asc'
      
      if (filter === 'todos') {
        data = await preguntasAPI.listar(page, 10, sortDirection)
      } else {
        data = await preguntasAPI.listarPorStatus(filter, page, 10, sortDirection)
      }
      setPreguntas(data.content)
      setTotalPages(data.totalPages)
    } catch (error) {
      console.error('Error al cargar preguntas:', error)
      setError(error.message || 'Error al cargar preguntas')
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
      <div className="flex items-center justify-between">
        <h1 className="text-3xl font-bold">Preguntas del Foro</h1>
        <div className="flex flex-col space-y-2">
          <div className="flex space-x-2">
            <Button
              variant={filter === 'todos' ? 'default' : 'outline'}
              onClick={() => { setFilter('todos'); setPage(0) }}
            >
              Todas
            </Button>
            <Button
              variant={filter === 'SIN_RESPUESTA' ? 'default' : 'outline'}
              onClick={() => { setFilter('SIN_RESPUESTA'); setPage(0) }}
            >
              Sin Respuesta
            </Button>
            <Button
              variant={filter === 'RESPONDIDA' ? 'default' : 'outline'}
              onClick={() => { setFilter('RESPONDIDA'); setPage(0) }}
            >
              Respondidas
            </Button>
            <Button
              variant={filter === 'RESUELTA' ? 'default' : 'outline'}
              onClick={() => { setFilter('RESUELTA'); setPage(0) }}
            >
              Resueltas
            </Button>
          </div>
          <div className="flex items-center space-x-2">
            <span className="text-sm text-muted-foreground">Ordenar:</span>
            <Select
              value={sortOrder}
              onChange={(e) => { setSortOrder(e.target.value); setPage(0) }}
              className="w-48"
            >
              <option value="recientes">Más recientes primero</option>
              <option value="antiguas">Más antiguas primero</option>
            </Select>
          </div>
        </div>
      </div>

      {loading ? (
        <div className="text-center py-12">
          <div className="text-gray-500">Cargando preguntas...</div>
        </div>
      ) : error ? (
        <Card>
          <CardContent className="py-12 text-center">
            <p className="text-red-500">Error: {error}</p>
            <p className="text-sm text-gray-500 mt-2">Verifica que el backend esté corriendo en http://localhost:8080</p>
          </CardContent>
        </Card>
      ) : preguntas.length === 0 ? (
        <Card>
          <CardContent className="py-12 text-center">
            <MessageSquare className="h-12 w-12 mx-auto mb-4 text-muted-foreground" />
            <p className="text-muted-foreground">No hay preguntas aún</p>
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
                        <User className="h-4 w-4" />
                        <span>{pregunta.autorNombre}</span>
                      </div>
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

export default Home
