import { useState, useEffect } from 'react'
import { Link } from 'react-router-dom'
import { usuariosAPI } from '../services/api'
import Button from '../components/ui/Button'
import { Card, CardHeader, CardTitle, CardDescription, CardContent } from '../components/ui/Card'
import { Users, MessageSquare, ChevronRight } from 'lucide-react'

const Usuarios = () => {
  const [usuarios, setUsuarios] = useState([])
  const [loading, setLoading] = useState(true)
  const [page, setPage] = useState(0)
  const [totalPages, setTotalPages] = useState(0)

  useEffect(() => {
    loadUsuarios()
  }, [page])

  const loadUsuarios = async () => {
    setLoading(true)
    try {
      const data = await usuariosAPI.listar(page, 20)
      setUsuarios(data.content)
      setTotalPages(data.totalPages)
    } catch (error) {
      console.error('Error al cargar usuarios:', error)
    } finally {
      setLoading(false)
    }
  }

  return (
    <div className="space-y-6">
      <h1 className="text-3xl font-bold">Usuarios</h1>

      {loading ? (
        <div className="text-center py-12">
          <div className="text-muted-foreground">Cargando usuarios...</div>
        </div>
      ) : usuarios.length === 0 ? (
        <Card>
          <CardContent className="py-12 text-center">
            <Users className="h-12 w-12 mx-auto mb-4 text-muted-foreground" />
            <p className="text-muted-foreground">No hay usuarios registrados</p>
          </CardContent>
        </Card>
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-4">
          {usuarios.map((usuario) => (
            <Link key={usuario.id} to={`/usuarios/${usuario.id}/preguntas`} state={{ usuario }}>
              <Card className="hover:shadow-md transition-shadow cursor-pointer h-full">
                <CardHeader>
                  <div className="flex items-start justify-between">
                    <div className="flex-1">
                      <CardTitle className="text-lg mb-2">{usuario.nombre}</CardTitle>
                      <CardDescription>@{usuario.nombreUsuario}</CardDescription>
                    </div>
                    <ChevronRight className="h-5 w-5 text-muted-foreground ml-2 mt-1" />
                  </div>
                </CardHeader>
                <CardContent>
                  <div className="flex items-center space-x-1 text-sm text-muted-foreground">
                    <MessageSquare className="h-4 w-4" />
                    <span>Ver preguntas del usuario</span>
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

export default Usuarios
