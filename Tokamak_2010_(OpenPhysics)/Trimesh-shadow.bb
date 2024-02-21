Include "fps.bb"
Include "physics.bb"
Include "shadowsystem.bb"
Include "postprocess.bb"

;------ Initialize Graphics --------
; Set Graphics, camera and light
Graphics3D 800, 600, 32, 2
;Graphics3D 1280, 1024, 32, 1
;Graphics3D 1280,800,32,1

;globals
Global DetailsEnable=1
Global ShadowsEnable=1

AppTitle "Open Physics. (Tokamak)"
AntiAlias True
SetBuffer BackBuffer()
SeedRnd MilliSecs()

;----camera / start
Global bgr=255,bgg=255,bgb=255
center	=	CreatePivot()
cam		=	CreateCamera()
			;initializes
			InitGlow(Cam)

			CameraClsColor cam,bgr,bgg,bgb
			CameraFogColor cam,bgr,bgg,bgb
			CameraFogMode cam,1
			CameraFogRange cam,10,500
			CameraRange cam,.2,100000
			CreateListener Cam, .05
			PositionEntity cam,15,10,-15
			PointEntity cam,center
			
			EntityRadius cam,1
			EntityType cam,COLL_CAM	
			;hud
			logo=opSetLogo(cam):DisableCaster(logo);disable shadowcasting on hud
;----camera / end

;----postprocess / start
			fade#=0.45
			dark_passes=2
			glow_passes=3
			glare_size#=4
			glow_r=255
			glow_g=255
			glow_b=255
;----postprocess / end
	
;----shadow / start
			ShadowTexSize=512;<-shadow mapp size (tip: for 1024x768 use 512)
			;ShadowTexSize=1024;<-shadow mapp size (tip: for 1024x768 use 512)
			SoftShadow=0;<-soft shadows?
			SoftShadowQuality=2
			SoftShadowRadius=2
			ShadowScale#=10;3.0
			ShadowRange#=3000;<-default shadow range
			Global AR#=90,AG#=100,AB#=127.5;<-ambient light color
			;Global AR#=50,AG#=42,AB#=39
			AmbientLight AR#,AG#,AB#
			ShadowAR#=AR#:ShadowAG#=AG#:ShadowAB#=AB#;match shadow color to world ambient
			InitShadowSystem();<--- Start Shadow System
;----shadow / end

;----lightning / start
Global lightPiv,light
lightPiv=	CreatePivot()
light	=	CreateLight(1,lightPiv)
			PositionEntity light,-50,30,30
;Local vis_light = CreateSphere(12,light):ScaleMesh vis_light,.1,.1,.1 	;<--- show the light source.
			;EntityFX vis_light,1
			SetLight(light,1);<--- Cast Light.
			RotateEntity light,90,0,0			
;light2	=	CreateLight()
;			LightColor light2,88,126,147
;			RotateEntity light2,50,-30,-30	
;----lightning / end
		
;----textures / start		
groundtex=LoadTexture("ground.jpg")	
boxtex=LoadTexture("box.jpg")	
balltex=LoadTexture("ball2.jpg")	
cyltex=LoadTexture("metal.jpg")				
;----textures / end

;----sounds / start
Global snd_hit	=	Load3DSound("sound\Bounce.wav")
;----sounds / end

; Create physic world
opWorldCreate(1)

;----scene / start
; Level		
mesh =	LoadMesh("slopeground1.b3d")
		SetReceiver(mesh)
		opBodySetGfx(mesh,groundtex,255,255,255,0)
		TriMeshBody = opCreateTrimesh(Mesh)

; Sky
sky	=	CreateSphere(5)
		ScaleEntity sky, -1000, -1000, -1000
		EntityTexture sky, LoadTexture("Skybox.jpg", 385)
		EntityFX sky, 9
		
; Just a shadow caster	
;car	=	LoadMesh("EVO8\body.b3d")
;		PositionEntity car,0,6,0

; DynamicObjects
cout=20
rad=10
height=10
For i = 1 To cout
	obj.tok	=	opBodyCreateBox(1,1,1,5)
				opBodySetPos(obj\body,Rnd(-rad,rad),Rnd(height,height+3),Rnd(-rad,rad))
				opBodySetVel(obj\body,Rnd(-2,2),Rnd(-2,2),Rnd(-2,2))
				opBodySetRot(obj\body,Rnd(-180,180),Rnd(-180,180),Rnd(-180,180))
				opBodySetGfx(obj\mesh,boxtex,255,255,255,0)

	

;Next
;For i = 1 To 20
	obj.tok	=	opBodyCreateSphere(.5,5)
				opBodySetPos(obj\body,Rnd(-rad,rad),Rnd(height,height+3),Rnd(-rad,rad))
				opBodySetVel(obj\body,Rnd(-2,2),Rnd(-2,2),Rnd(-2,2))
				opBodySetRot(obj\body,Rnd(-180,180),Rnd(-180,180),Rnd(-180,180))
				opBodySetGfx(obj\mesh,balltex,255,255,255,0)

				
;Next

;For i = 1 To 20
	obj.tok	=	opBodyCreateCapsule(.5,1,5)
				opBodySetPos(obj\body,Rnd(-rad,rad),Rnd(height,height+3),Rnd(-rad,rad))
				opBodySetVel(obj\body,Rnd(-2,2),Rnd(-2,2),Rnd(-2,2))
				opBodySetRot(obj\body,Rnd(-180,180),Rnd(-180,180),Rnd(-180,180))
				opBodySetGfx(obj\mesh,cyltex,255,255,255,0)

Next
;----scene / end

;-----------------------------------
Const COLL_PHYS = 1, COLL_CAM = 2, COLL_PED = 3
Collisions COLL_PED, COLL_PHYS, 2, 3
Collisions COLL_CAM, COLL_PHYS, 2, 3


;Main Loop
While Not KeyHit(1)

	;----------------------------------------------------------------

	;Move The camera
	FreeLook(cam,.1)
	;TurnEntity car,0,1,0
	PointEntity light, center
	
	; Update world
	UpdateWorld
		
	;----Physics Update / start
	;Apply Random Force
	If KeyHit(57)
		For p.tok = Each tok
			TOKRB_ApplyImpulse(p\body, 0, Rnd(10, 20), 0)
			TOKRB_ApplyTwist p\body, Rnd(-5, 5), Rnd(-5, 5), Rnd(-5, 5)
		Next
	EndIf
	;opGeomSetEnt(temp_g,temp_m)
	
	;respond when collided	
	For p.tok = Each tok
		opRespondContacts(p\body,p\mesh,cam)
	Next
	
	; update physics & Position physic Box to Entity
	pms = MilliSecs()
	opWorldStep(0.025)
	Pyhsic_time = MilliSecs() - pms
	;----Physics Update / end
	
	;----PostProcess Update / start
	RenderGlow(fade#,dark_passes,glow_passes,glare_size#,glow_r,glow_g,glow_b)
	;----PostProcess Update / end
		
	;----Shadow Update / start	
	PointEntity light,lightPiv;<--- direct the light to the center.
	PositionEntity lightPiv,EntityX(cam),0,EntityZ(cam);clamp shadow pivot to cameras cordinates
	;hide stuff before shadow update
	HideEntity sky
	HideEntity GL_sprite_scene;hide glow
	
	If ShadowsEnable=1 Then UpdateShadowSystem(cam);<--- UpdateShadows.
	;show stuff after shadow update
	ShowEntity GL_sprite_scene;show glow
	ShowEntity sky
	;----Shadow Update / end		

		
	; Render world
	rms = MilliSecs()	
	RenderWorld
	Render_time = MilliSecs() - rms

	;----HUD / start
	Color 250,250,50
	If details=1
	Text 20,20,"FPS = "+Fps(50)
	Text 20,40,"Physics = "+Pyhsic_time
	Text 20,60,"Render = "+Render_time	
	Text 20,80,"bodys = "+opBodyCount	
	End If
	;----HUD / end		
	Flip 1

Wend
FreeShadowSystem();<--- kill shadow system
opDestroyWorld();<--- kill physics engine
End

Function opRespondContacts(body,mesh,cam)	
	ncoll = TOKSIM_GetCollisionCount()
	For i=0 To ncoll-1
		
		If (opCollBodyA(colbank,i)=body And TOKRB_GetUserData(opCollBodyB(colbank,i))=0) Or (opCollBodyB(colbank,i)=body And TOKRB_GetUserData(opCollBodyB(colbank,i))=0) Then
			depth#=opGetCollisionDepth(colbank,i)
			;DebugLog depth#
			;here is the colision response part
			;apply sound			
			If Abs(depth)>.7 Then
				c=EmitSound(snd_hit,mesh)
				ChannelVolume c, Abs(depth)  * .15
			EndIf
			
			;render contacts(particles etc)
			RenderContacts=1
			If RenderContacts=1
			Color r,g,b
			
			x# = opGetCollisionAX(colbank,i)
			y# = opGetCollisionAY(colbank,i)
			z# = opGetCollisionAZ(colbank,i)
			CameraProject cam,x,y,z
			sx# =ProjectedX():sy# =ProjectedY()
			;Oval sx-2,sy-2,4,4
			x# = x# + opGetCollisionNX(colbank,i)
			y# = y# + opGetCollisionNY(colbank,i)
			z# = z# + opGetCollisionNZ(colbank,i)
			CameraProject cam,x,y,z
			;sx2# =ProjectedX():sy2# =ProjectedY()
			;Line sx,sy,sx2,sy2+(depth#*50)
			;Text sx,sy,depth#
			;Color 255,255,255
			End If
		EndIf

	Next

End Function