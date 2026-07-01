import { useState, useEffect } from 'react'
import { useParams } from 'react-router-dom'
import { preguntasAPI, respuestasAPI } from '../services/api'
import Button from '../components/ui/Button'
import Input from '../components/ui/Input'
import Textarea from '../components/ui/Textarea'
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '../components/ui/Card'
import Badge from '../components/ui/Badge'
import { User, Clock, MessageSquare, Send, CheckCircle } from 'lucide-react'

const PreguntaDetail = () => {
  const { id } = useParams()
  const [pregunta, setPregunta] = useState(null)
  const [respuestas, setRespuestas] = useState([])
  const [loading, setLoading] = useState(true)
  const [nuevaRespuesta, setNuevaRespuesta] = useState('')
  const [enviando, setEnviando] = useState(false)

  useEffect(() => {
    loadPregunta()
  }, [id])

  const loadPregunta = async () => {
    setLoading(true)
    try {
      const preguntaData = await preguntasAPI.obtener(id)
      setPregunta(preguntaData)
      
      // Cargar respuestas por separado
      const respuestasData = await respuestasAPI.listarPorPregunta(id)
      setRespuestas(respuestasData.content || [])
    } catch (error) {
      console.error('Error al cargar pregunta:', error)
    } finally {
      setLoading(false)
    }
  }

  const handleSubmitRespuesta = async (e) => {
    e.preventDefault()
    if (!nuevaRespuesta.trim()) return

    setEnviando(true)
    try {
      const respuesta = await respuestasAPI.crear({
        mensaje: nuevaRespuesta,
        preguntaId: id,
        autorId: pregunta.autorId
      })
      setRespuestas([...respuestas, respuesta])
      setNuevaRespuesta('')
    } catch (error) {
      console.error('Error al crear respuesta:', error)
    } finally {
      setEnviando(false)
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

  if (loading) {
    return (
      <div className="text-center py-12">
        <div className="text-muted-foreground">Cargando pregunta...</div>
      </div>
    )
  }

  if (!pregunta) {
    return (
      <Card>
        <CardContent className="py-12 text-center">
          <p className="text-muted-foreground">Pregunta no encontrada</p>
        </CardContent>
      </Card>
    )
  }

  return (
    <div className="space-y-6">
      <Card>
        <CardHeader>
          <div className="flex items-start justify-between">
            <div className="flex-1">
              <CardTitle className="text-2xl mb-2">{pregunta.titulo}</CardTitle>
              <CardDescription className="text-base whitespace-pre-wrap">
                {pregunta.mensaje}
              </CardDescription>
            </div>
            <Badge className={getStatusColor(pregunta.status)}>
              {pregunta.status}
            </Badge>
          </div>
        </CardHeader>
        <CardContent>
          <div className="flex items-center space-x-4 text-sm text-muted-foreground">
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
              <span>{respuestas.length} respuestas</span>
            </div>
          </div>
        </CardContent>
      </Card>

      <div className="space-y-4">
        <h2 className="text-xl font-semibold">Respuestas ({respuestas.length})</h2>

        {respuestas.length === 0 ? (
          <Card>
            <CardContent className="py-8 text-center">
              <MessageSquare className="h-8 w-8 mx-auto mb-2 text-muted-foreground" />
              <p className="text-muted-foreground">No hay respuestas aún. ¡Sé el primero!</p>
            </CardContent>
          </Card>
        ) : (
          respuestas.map((respuesta) => (
            <Card key={respuesta.id}>
              <CardContent className="pt-6">
                <div className="flex items-start justify-between mb-3">
                  <div className="flex items-center space-x-2">
                    <User className="h-4 w-4 text-muted-foreground" />
                    <span className="font-medium">{respuesta.autorNombre}</span>
                  </div>
                  {respuesta.solucion && (
                    <Badge className="bg-green-500">
                      <CheckCircle className="h-3 w-3 mr-1" />
                      Solución
                    </Badge>
                  )}
                </div>
                <p className="text-sm whitespace-pre-wrap mb-3">{respuesta.mensaje}</p>
                <div className="flex items-center space-x-1 text-xs text-muted-foreground">
                  <Clock className="h-3 w-3" />
                  <span>{formatDate(respuesta.fechaCreacion)}</span>
                </div>
              </CardContent>
            </Card>
          ))
        )}
      </div>

      <Card>
        <CardHeader>
          <CardTitle className="text-lg">Agregar Respuesta</CardTitle>
        </CardHeader>
        <CardContent>
          <form onSubmit={handleSubmitRespuesta} className="space-y-4">
            <Textarea
              placeholder="Escribe tu respuesta aquí..."
              value={nuevaRespuesta}
              onChange={(e) => setNuevaRespuesta(e.target.value)}
              rows={4}
              required
            />
            <Button type="submit" disabled={enviando || !nuevaRespuesta.trim()}>
              <Send className="h-4 w-4 mr-2" />
              {enviando ? 'Enviando...' : 'Enviar Respuesta'}
            </Button>
          </form>
        </CardContent>
      </Card>
    </div>
  )
}

export default PreguntaDetail
