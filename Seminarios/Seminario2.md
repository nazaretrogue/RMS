# Seminario RMS

1. Cambiar la IP del dispositivo

```bash
sudo ifconfig <nombre_interfaz> <nueva_ip>/<mascara>
```

2. Nos conectamos al router (user: laboratorio, password: telematica)

```bash
telnet <ip_router>
```

3. Mosstramos las interfaces de red

```bash
show interfaces
```

4. Y su estado

```bash
show interfaces status
```

5. Entramos en modo configuración

```bash
configure terminal
```

6. Usamos el siguiente comando para crear una clase para ofrecer QoS

```bash
class-map
```

7. Que coincidan todos las sentencias con un AND lógico

```bash
class-map match-all <nombre(CLASE-WEB)>
```

8. Estamos en el modo configuración de la clase. Añadimos descripcion

```bash
description "Clase de ..."
```

9. Creamos un criterio para clasificar las sentencias según el protocolo http

```bash
match protocol http
```

10. Salimos al modo normal del router (Rx_y#)

11. Mostramos las listas de acceso del router

```bash
show access-lists
```

12. Creamos una lista que permita el tráfico tcp y cualquiera que provenga del puerto 8000

```bash
access-lists 101 permit tcp any eq 8000 any
```

13. Accedemos a configurar la clase

```bash
class-map match-all CLASE-WEB
```

14. Que se produzca el filtrado si el tráfico es de la lista de acceso que acabamos de crear

```bash
match access-group 101
```

15. Salimos del modo de configuración de la clase

16. Creamos una política

```bash
policy-map <nombre_politica(POLITICA-CONSERVADORA)>
```

17. Elegimos la clase sobre la que vamos a aplicar la política

```bash
class <nombre>
```

18. Modificamos por ejemplo los bits por segundo

```bash
shape average 1000000
```

19. Salimos al modo normal del router (Rx_y#)

20. Mostramos la política

```bash
show policy-map
```

********************************************************************************
********************************************************************************
********************************************************************************

21. Entramos a configurar de nuevo el router

```bash
configure terminal
```

22. Creamos otra clase para VoIP

```bash
class-map match-all CLASE-VOIP
```

23. Creamos una lista de acceso

```bash
access-list 102 permit udp any eq 554 any
```

24. Usamos la política que hemos creado antes

```bash
policy-map <nombre_politica>
```

25. Elegimos la clase que vamos a modificar

```bash
class CLASE-VOIP
```

26. Modificamos la política para esta clase

```bash
police 8000 1000 conform-action transmit exceed-action drop
```

27. Salimos al modo normal del router (Rx_y#)

28. Mostramos las políticas

```bash
policy-map
```

29. Entramos a configurar de nuevo el router

```bash
configure terminal
```

30. Entramos a configurar la interfaz deseada

```bash
interface fastEthernet 0/1
```

31. Configuramos la política del servicio para ofrecer QoS en la salida

```bash
service-policy output <nombre_politica>
```

32. Salimos al modo normal del router

```bash
show interfaces fastEthernet 0/1
```

33. Entramos a configurar el router de nuevo

```bash
configure-terminal
```

34. Creamos una nueva política de prueba

```bash
policy-map POLITICA-PRUEBA
```

35. Configuramos la clase interfaz

```bash
interface fastEthernet 0/1
```

36. Establecemos un rate que no tiene ningún sentido, solo por probar

```bash
rate-limit output 8000 1000 2000 conform-action transmit exceed_action transmit
```
