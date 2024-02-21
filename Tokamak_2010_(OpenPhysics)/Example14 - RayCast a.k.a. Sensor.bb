Include "fps.bb"
Include "physics.bb"
;------ Initialize Graphics --------
; Set Graphics, camera and light
Graphics3D 800,600,32,2

AppTitle "Open Physics. Example14 / RayCast a.k.a. Sensor"
AntiAlias True
SetBuffer BackBuffer()
SeedRnd MilliSecs()

center	=	CreatePivot()
cam		=	CreateCamera()
			CameraClsColor cam,255,255,255
			CameraFogColor cam,255,255,255
			CameraFogMode cam,1
			CameraFogRange cam,50,200
			CameraRange cam,.2,100000
			CreateListener Cam, .1
			PositionEntity cam,0,15,-15
			PointEntity cam,center
			
			EntityRadius cam,1
			EntityType cam,COLL_CAM	
			opSetLogo(cam)
					
			AmbientLight 50,42,39
light1	=	CreateLight()
			LightColor light1,228,224,187
			RotateEntity light1,20,50,0
light2	=	CreateLight()
			LightColor light2,88,126,147
			RotateEntity light2,-45,180+45,0			

;textures
Global checktex1,checktex2,checktex
		
checktex1=	CreateCheckerTex()
			ScaleTexture checktex1,5,5

checktex2=	CreateCheckerTex()
			ScaleTexture checktex2,.5,.5
						
checktex=	CreateCheckerTex()
			ScaleTexture checktex,1,1
			
; Create mirror plane
plane	=	CreatePlane(16)
			opBodySetGfx(plane,checktex1)
			EntityAlpha plane,.8
			CreateMirror

; Create physic world
opWorldCreate(1)

; Create cube pyramid
size=3
Spacing = 0
Cubesize#=1
pos_y# = 1
Offset = -size * (Cubesize*2 + Spacing) * 0.5
While size
		For  i=0 To size-1

		Pos_x=	Offset + i * (CubeSize*2 + Spacing)					
		box.tok=opbodyCreateBox(CubeSize*2,CubeSize*2,CubeSize*2,1)					
   				opBodySetPos(box\body,pos_x, pos_y,0)
				opBodySetGfx(box\mesh,checktex,Rand(150, 255), Rand(150, 255), Rand(150, 255))					
				
		Next
	Offset = Offset +Cubesize;
	pos_y =  pos_y +(Cubesize*2 + Spacing)
	size=size-1
Wend

; Create Ray Casting body
ray.tok	=	opbodyCreateBox(2,2,2,0)					
   			opBodySetPos(ray\body,0, 10,0)
			opBodySetGfx(ray\mesh,checktex,255,255,0)

RayX#=0
RayY#=-1
RayZ#=0
RayDY#=-10
;Create Ray sensor
raySensor	=	opCreateRay(ray\body,RayX#,RayY#,RayZ#,0,RayDY#,0)

;Ray mesh
sensormesh	=	CreateCylinder(8,True,ray\mesh)
				ScaleEntity sensormesh,0.1,(Abs(RayDY#))/2,0.1
				PositionEntity sensormesh,RayX#,RayY#+(RayDY#/2),RayZ#
				EntityAlpha sensormesh,0.2 

;pointer
RCPointer	=	CreateCube()
				PositionMesh RCPointer,0,0,1
				ScaleMesh RCPointer,.05,.05,1
				EntityColor RCPointer,0,255,0
sph			=	CreateSphere()
				ScaleMesh sph,.2,.2,.2
				AddMesh sph,RCPointer
				FreeEntity sph

;-----------------------------------
Const COLL_PHYS = 1, COLL_CAM = 2, COLL_PED = 3
Collisions COLL_PED, COLL_PHYS, 2, 3
Collisions COLL_CAM, COLL_PHYS, 2, 3

;Main Loop
While Not KeyHit(1)

	;----------------------------------------------------------------
	
	;Move The camera
	FreeLook(cam,.1)
	
	;Move  RayCaster	 
	If KeyDown (205) x# = x+.1
	If KeyDown (203) x# = x-.1
	If KeyDown (200) z# = z+.1
	If KeyDown (208) z# = z-.1
	If KeyDown (201) y# = y+.1
	If KeyDown (209) y# = y-.1


	
	; RayCast
	opBodySetPos(ray\body,x,10+y,z)
	DetectDepth#=opRayGetDepth(raySensor)
	If DetectDepth>0.0 Then
		; position RayCast Pointer
		PositionEntity RCPointer,opRayGetX(raySensor),opRayGetY(raySensor),opRayGetZ(raySensor)
		AlignToVector RCPointer,opRayGetNX(raySensor),opRayGetNY(raySensor),opRayGetNZ(raySensor),3
		EntityColor sensormesh,255,0,0
		ShowEntity RCPointer
		; adding force
		If KeyHit(57)
			TOKRB_ApplyImpulse(TOKSENSOR_GetDetectRigidBody(raySensor),0,100,0);not working, ask sweenie
		EndIf
	Else		
		HideEntity RCPointer
		EntityColor sensormesh,255,255,255
	EndIf
	
	;TFormPoint 0,0,1000,cam,0
	;TOKSENSOR_SetLineSensor(raySensor,EntityX(cam),EntityY(cam),EntityZ(cam),TFormedX(),TFormedY(),TFormedZ())
	;If phRayCast(EntityX(cam),EntityY(cam),EntityZ(cam),TFormedX(),TFormedY(),TFormedZ())
		; position RayCast Pointer
		;PositionEntity RCPointer,TOKSENSOR_GetLineUnitVectorX(raySensor),TOKSENSOR_GetLineUnitVectorY(raySensor),TOKSENSOR_GetLineUnitVectorZ(raySensor)
		;PositionEntity RCPointer,TOKSENSOR_GetLinePosX(raySensor),TOKSENSOR_GetLinePosY(raySensor),TOKSENSOR_GetLinePosZ(raySensor)
		;PositionEntity RCPointer,TOKSENSOR_GetLinePosX(raySensor)-TOKSENSOR_GetLineUnitVectorX(raySensor),TOKSENSOR_GetLinePosY(raySensor)-TOKSENSOR_GetLinePosY(raySensor),TOKSENSOR_GetLinePosZ(raySensor)-TOKSENSOR_GetLinePosZ(raySensor)

	;	AlignToVector RCPointer,phRayGetNX(),phRayGetNY(),phRayGetNZ(),3
	;	ShowEntity RCPointer
		; adding force
		;If KeyHit(57)
		;	TOKRB_ApplyImpulse(TOKSENSOR_GetDetectRigidBody(raySensor),0,100,0)
		;	For p.tok= Each tok
		;	TOKRB_ApplyImpulse(p\Body,0,10,0)
		;	Next
		;EndIf
	;Else
	;	HideEntity RCPointer
	;EndIf
	
	; update physics & Position physic Box to Entity
	pms = MilliSecs()	
	opWorldStep(0.025)
	Pyhsic_time = MilliSecs() - pms	
	
	; Render world
	ms = MilliSecs()
	UpdateWorld
	RenderWorld
	Render_time = MilliSecs() - ms

	Color 50,50,50
	Text 20,20,"FPS = "+Fps(50)
	Text 20,40,"Physics = "+Pyhsic_time
	Text 20,60,"Render = "+Render_time
	Text 20,80,"Render = "+opBodysCount	

	Text 20,100,"Ray_GetLineVectorX = "+TOKSENSOR_GetLineUnitVectorX(raySensor)
	Text 20,120,"Ray_GetLineVectory = "+TOKSENSOR_GetLineUnitVectorY(raySensor)
	Text 20,140,"Ray_GetLineVectorz = "+TOKSENSOR_GetLineUnitVectorZ(raySensor)
	Text 20,160,"Ray_GetLinePosX = "+TOKSENSOR_GetLinePosX(raySensor)
	Text 20,180,"Ray_GetLinePosy = "+TOKSENSOR_GetLinePosY(raySensor)
	Text 20,200,"Ray_GetLinePosz = "+TOKSENSOR_GetLinePosZ(raySensor)
	Text 20,220,"Ray_GetDetectNormalX = "+TOKSENSOR_GetDetectNormalX(raySensor)
	Text 20,240,"Ray_GetDetectNormaly = "+TOKSENSOR_GetDetectNormalY(raySensor)
	Text 20,260,"Ray_GetDetectNormalz = "+TOKSENSOR_GetDetectNormalZ(raySensor)		
	Text 20,280,"Ray_GetDetectContactPointX = "+TOKSENSOR_GetDetectContactPointX(raySensor)
	Text 20,300,"Ray_GetDetectContactPointy = "+TOKSENSOR_GetDetectContactPointY(raySensor)
	Text 20,320,"Ray_GetDetectContactPointz = "+TOKSENSOR_GetDetectContactPointZ(raySensor)
	Text 20,340,"Ray_GetDetectRigidBody = "+TOKSENSOR_GetDetectRigidBody(raySensor)
	Text 20,360,"Ray_GetDetectAnimatedBody = "+TOKSENSOR_GetDetectAnimatedBody(raySensor)
	Text 20,380,"Ray_GetDetectMaterial = "+TOKSENSOR_GetDetectMaterial(raySensor)
	Text 20,400,"DetectDepth# = "+DetectDepth#
				
	Flip 1

Wend
opDestroyWorld()
End
