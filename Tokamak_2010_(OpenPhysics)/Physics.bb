;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
; Physics functions
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
Const seg=16
Const Rigid_Bodies=10000,Animated_Bodies=1000,Particle_Count=10000
Const infinity=9999999999999999
Const TOK_COLL_DYNAMIC=1,TOK_COLL_STATIC=2,TOK_COLL_PARTICLE=3

Global opBodyCount,opJointCount,opParticleCount
Global colbank

Type tok
	Field body
	Field geom
	Field mesh
	Field mass
	Field sound	
End Type

;create world
Function opWorldCreate(plane)
	TOKSIM_SetRigidBodiesCount Rigid_Bodies
	TOKSIM_SetAnimatedBodiesCount Animated_Bodies
	;TOKSIM_SetRigidParticleCount Particle_Count ;disabled because particles use rigid bodies
	;TOKSIM_SetControllersCount Controllers_Count
	TOKSIM_SetOverlappedPairsCount (Rigid_Bodies*(Animated_Bodies-1))/2
	TOKSIM_SetGeometriesCount Rigid_bodies+Animated_Bodies;+Particle_Count+Controllers_Count ;Assuming each one only has one geometry.  Not true with more complex rigid bodies and animated bodies.
	TOKSIM_CreateSimulator(0,-9.89,0)
	;setup collision bank
	colbank=CreateBank(104*(Rigid_bodies+Animated_Bodies+50))
	TOKSIM_SetCInfoBank colbank
	TOKSIM_SetCollisionResponse TOK_COLL_DYNAMIC,TOK_COLL_DYNAMIC,3
	TOKSIM_SetCollisionResponse TOK_COLL_DYNAMIC,TOK_COLL_STATIC,3
	TOKSIM_SetCollisionResponse TOK_COLL_PARTICLE,TOK_COLL_PARTICLE,0

	If plane=1 Then
		;plane 
		x=1000000
		z=1000000
		ground = TOKAB_Create()
		TOKAB_AddBox(ground,x*2,10.0,z*2.0)
		TOKAB_SetPosition(ground,0.0,-5.0,0.0)
		TOKRB_SetCollisionID(ground,TOK_COLL_STATIC)
	EndIf
	
End Function

;set timestep
Function opWorldStep(dtime# = .025,sub=1)
	If dtime# > 0 TOKSIM_Advance(dtime#,sub)
	For p.tok = Each tok
		If p\sound Then
			;opSetCollisionResponse(p\body,p\mesh)
		End If
		opBodySetEnt(p\body,p\mesh)
	Next
	;For s.sode = Each sode
		;opGeomSetEnt(s\geom,s\mesh)
	;Next
End Function

Function opDestroyWorld()
	TOKSIM_DestroySimulator()
End Function

;set the entity to the body
Function opBodySetEnt(body,ent)
		PositionEntity ent,TOKRB_GetX#(body),TOKRB_GetY#(body),TOKRB_GetZ#(body) 
 		RotateEntity ent,TOKRB_GetPitch#(body),TOKRB_GetYaw#(body),TOKRB_GetRoll#(body),False
End Function

;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
; Rigid Bodys
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------

; create box primitive body
Function opBodyCreateBox.tok(w#,h#,d#,mass#)
	Local p.tok
	p = New tok
	p\body = TOKRB_Create()
	
	p\geom = TOKRB_AddBox (p\body,w#,h#,d#)

	TOKRB_SetLinearDamping p\body,0.001
	TOKRB_SetAngularDamping p\body,0.02 	
	
	If mass#=0 Then		
		TOKRB_SetMass p\body,infinity
		TOKRB_GravityEnable(p\body,0)
		TOKRB_SetBoxInertiaTensor p\body,w#,h#,d#,infinity
	Else
		TOKRB_SetMass p\body,mass#
		TOKRB_SetBoxInertiaTensor p\body,w#,h#,d#,mass#
	End If
	
	TOKRB_UpdateBoundingInfo(p\body)
	TOKRB_SetCollisionID(p\body,TOK_COLL_DYNAMIC)
	p\mass=mass		
	p\mesh = CreateCube()
	ScaleMesh p\mesh,w#*.5,h#*.5,d#*.5	
	opBodyCount=opBodyCount+1
	Return p
End Function

; create sphere primitive body
Function opBodyCreateSphere.tok(radius#,mass#)
	Local p.tok
	p = New tok
	p\body = TOKRB_Create();ODE_dBodyCreate()
	
	p\geom = TOKRB_AddSphere (p\body,radius#*2);w#*2,h#*2,d#*2
	TOKRB_SetLinearDamping p\body,0.001
	TOKRB_SetAngularDamping p\body,0.02	
	
	If mass#=0 Then		
		TOKRB_SetMass p\body,infinity
		TOKRB_GravityEnable(p\body,0)
		TOKRB_SetSphereInertiaTensor p\body,radius#*2,infinity
	Else
		TOKRB_SetMass p\body,mass#
		TOKRB_SetSphereInertiaTensor p\body,radius#*2,mass#
	End If	
	
	TOKRB_UpdateBoundingInfo(p\body)
	TOKRB_SetCollisionID(p\body,TOK_COLL_DYNAMIC)
	p\mass=mass
	p\mesh = CreateSphere(seg)
	ScaleMesh p\mesh,radius#,radius#,radius#
	opBodyCount=opBodyCount+1
	Return p
End Function

; Create mesh and body for a capsule
Function opBodyCreateCapsule.tok(radius#, height#,mass#)
	Local p.tok
	p = New tok
	p\body = TOKRB_Create()	
	p\geom = TOKRB_AddCylinder (p\body,radius#*2, height#)
	TOKRB_SetLinearDamping p\body,0.001
	TOKRB_SetAngularDamping p\body,0.02 	

	If mass#=0 Then		
		TOKRB_SetMass p\body,infinity
		TOKRB_GravityEnable(p\body,0)
		TOKRB_SetCylinderInertiaTensor p\body,radius#*2,height#,infinity
	Else
		TOKRB_SetMass p\body,mass#
		TOKRB_SetCylinderInertiaTensor p\body,radius#*2,height#,mass#
	End If
	
	TOKRB_UpdateBoundingInfo(p\body)
	TOKRB_SetCollisionID(p\body,TOK_COLL_DYNAMIC)	
	p\mass=mass
	p\mesh = CreateCapsule(seg,radius#,height#)
	opBodyCount=opBodyCount+1
	Return p
End Function
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
; Compound hull Bodys
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
; Create a compound body
Function opBodyCreateCompound.tok()
	Local p.tok
	p = New tok
	p\body = TOKRB_Create()	
		
	TOKRB_UpdateBoundingInfo(p\body)
	TOKRB_SetCollisionID(p\body,TOK_COLL_DYNAMIC)	
	p\mesh=CreateMesh()
	opBodyCount=opBodyCount+1
	Return p
End Function

;Add Compound Geom Box
Function opBodyAddCompoundBox(body,mesh,w#=1,h#=1,d#=1,x#=0,y#=0,z#=0,pitch#=0,yaw#=0,roll#=0)
	;phy	
	temp_g = TOKRB_AddBox(body,w#,h#,d#)
	TOKGEOM_SetPositionAndRotation(temp_g,x#,y#,z#,pitch#,yaw#,roll#)
	;3d	
	temp = CreateCube()
	ScaleMesh temp,w#*.5,h#*.5,d#*.5

	RotateMesh temp,pitch#,yaw#,roll#
		PositionMesh temp,x#,y#,z#	
	AddMesh temp,mesh;link the mesh
	FreeEntity temp
	Return geom
End Function

;Add Compound Geom Sphere
Function opBodyAddCompoundSphere(body,mesh,radius#=1,x#=0,y#=0,z#=0)	
	;phy
	temp_g = TOKRB_AddSphere(body,radius#*2)
	pitch#=0:yaw#=0:roll#=0
	TOKGEOM_SetPositionAndRotation(temp_g,x#,y#,z#,pitch#,yaw#,roll#)	
	;3d
	temp = CreateSphere()
	ScaleMesh temp,radius,radius,radius
	PositionMesh temp,x#,y#,z#
	AddMesh temp,mesh;link the mesh
	FreeEntity temp
	Return geom
End Function	

;Add Compound Geom Capsule
Function opBodyAddCompoundCapsule(body,mesh,radius#=1,height#=1,x#=0,y#=0,z#=0,pitch#=0,yaw#=0,roll#=0)	
	;phy
	temp_g = TOKRB_AddCylinder(body,radius#*2,height#)
	TOKGEOM_SetPositionAndRotation(temp_g,x#,y#,z#,pitch#-90,yaw#,roll#)
	;3d
	temp=CreateCapsule(8,radius#,height#)
	RotateEntity temp,pitch#-90,yaw#,roll#
	PositionMesh temp,x#,y#,z#	
	AddMesh temp,mesh;link the mesh
	FreeEntity temp
	Return geom
End Function


;Set Compound Mass			
Function opBodySetCompound(body,mesh,mass#=1,Debug=0)
	;SetMass & Debug
	mw#=MeshWidth(mesh)*.5
	mh#=MeshHeight(mesh)*.5
	md#=MeshDepth(mesh)*.5
	If Debug=1
		MassChk=CreateCube(mesh)
		ScaleMesh MassChk,mw#,mh#,md#
		EntityColor MassChk,0,255,0 
		EntityAlpha MassChk,.5
	End If
	If mass#=0 Then		
		TOKRB_SetMass body,infinity
		TOKRB_GravityEnable(body,0)
		TOKRB_SetBoxInertiaTensor body,mw#,mh#,md#,infinity
	Else
		TOKRB_SetMass body,mass#
		TOKRB_SetBoxInertiaTensor body,mw#,mh#,md#,mass#
	End If
	TOKRB_UpdateBoundingInfo body
	mass=mass
End Function
	
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
; Convex hull Bodys
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------

;include our hull generator lib
;requires qhull1.dll, wrapper and adjacency.exe
Include "TokaConvexGenerator.bb"
; - - usage
;file$="camel.x"
;mesh = LoadMesh(file$)
; - - way 1
;Convex = opBodyGenerateConvex(mesh,file$)
;Body = opBodyCreateHull(mesh,Convex,10)
; - - or way 2
;Body = opBodyMakeHull(mesh,file$,10)


;internal function
Function opLoadConvex(file$)
	Convex = CreateBank(FileSize(file$))
	filein = ReadFile(file$)
	For i=0 To FileSize(file$)-1
		PokeByte Convex,i,ReadByte(filein) 
	Next   
	CloseFile filein 
	Return Convex
End Function

;generate Convex body info
Function opBodyGenerateConvex(meshobj,file$)
	Hullfile$=file$+"_.hull"
	;create
	hull=create_tok_hull( meshobj, Hullfile$,  0.00, 1)	;create hull geometry
	HideEntity hull;<-hide the hull geometry	
	Convex = opLoadConvex(Hullfile$)
	Return Convex
End Function

;use convexbody info and create hull - use this if u mage lots of simmilar convex hulls
Function opBodyCreateHull.tok(meshobj,Convex,mass#=1)
	Local p.tok
	p = New tok
	
	mw#=MeshWidth(meshobj)*.5
	mh#=MeshHeight(meshobj)*.5
	md#=MeshDepth(meshobj)*.5
	bbb = TOKRB_Create();TOKRB_Create()
 	geom=TOKRB_AddConvex(bbb,Convex,BankSize(Convex))
	TOKRB_SetLinearDamping bbb,0.001
	TOKRB_SetAngularDamping bbb,0.02 	
	TOKRB_SetMass bbb,mass#
	TOKRB_SetBoxInertiaTensor bbb,mw#,mh#,md#,mass# 
	

	p\body = bbb
	p\geom = geom
	opBodyCount=opBodyCount+1	
	Return p
End Function

;quick function - use this for a single convex hull generation
Function opBodyMakeHull.tok(meshobj,file$,mass#=1)
	Local p.tok
	p = New tok

	mw#=MeshWidth(meshobj)*.5
	mh#=MeshHeight(meshobj)*.5
	md#=MeshDepth(meshobj)*.5
	Hullfile$=file$+"_.hull"
	; if not create one
	hull=create_tok_hull( meshobj, Hullfile$,  0.00, 1);create hull geometry
	HideEntity hull;<-hide the hull geometry				
	Convex = opLoadConvex(Hullfile$)
	;call wrapper function
	bbb = TOKRB_Create();TOKRB_Create()
 	geom=TOKRB_AddConvex(bbb,Convex,BankSize(Convex))
	TOKRB_SetLinearDamping bbb,0.001
	TOKRB_SetAngularDamping bbb,0.002 	
	TOKRB_SetMass bbb,mass#
	TOKRB_SetBoxInertiaTensor bbb,mw#,mh#,md#,mass#	
	

	p\body = bbb
	p\geom = geom
	opBodyCount=opBodyCount+1	
	Return p	
End Function

; Set the body's position.
Function opBodySetPos.tok(body,x#,y#,z#)
	If body=0 RuntimeError "TOKAMAK Warning: body not found!"
	TOKRB_SetPosition(body,x,y,z);p\body
End Function

; Set the body's rotation.
Function opBodySetRot.tok(body,x#,y#,z#)
	If body=0 RuntimeError "TOKAMAK Warning: body not found!"
	TOKRB_SetRotation(body,x,y,z);p\body
End Function

; Set the body's Velocity
Function opBodySetVel.tok(body,x#,y#,z#)
	If body=0 RuntimeError "TOKAMAK Warning: body not found!"
	TOKRB_SetVelocity(body,x#*4,y#*4,z#*4)
End Function

; Set body's sleep state				
Function opBodySetAutoSleep(body,d=1)
	If body=0 RuntimeError "TOKAMAK Warning: Cant set auto sleep state, body not found!"
	TOKRB_SetSleepingParameter(body,d)	
	;TOKRB_Active(body,d)	
End Function
	
; Get body's sleep state			
Function opBodyGetSleep(body)
	If body=0 RuntimeError "TOKAMAK Warning: Cant get sleep state, body not found!"
	sleep = TOKRB_IsIdle(Body)
	Return sleep
End Function

; Set body's Sleep Treshold
Function opBodySetAutoSleepTreshold(body,sleepparam#)
	If body=0 RuntimeError "TOKAMAK Warning: Cant set sleep Treshold state, body not found!"
	TOKRB_SetSleepingParameter(body,sleepparam#)
End Function

;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
; Joints
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------

;ball/universal(socket) joint
Function opJointBallCreate(x#=0,y#=0,z#=0,pitch#=0,yaw#=0,roll#=0,body2=0,body1=0,d#=0,e#=1,i#=100)	
	J = TOKJOINT_Create(3,body2,body1)
	TOKJOINT_SetPositionAndRotationWorld(J,x,y,z,pitch,yaw,roll)
	TOKJOINT_SetType(J,1)
	TOKJOINT_Enable(J,True)
	TOKJOINT_SetDampingFactor J,d#
	TOKJOINT_SetEpsilon J,e#
	TOKJOINT_SetIterations J,i#
	opJointCount=opJointCount+1
	Return J
End Function

Function opJointBallSetLimit(j,e1=0,MinAng#=0,MaxAng#=0,e2=0,MinAng2#=0,MaxAng2#=0)
	If j=0 RuntimeError "Tokamak Warning: There Was no Ball joint found!" 
	TOKJOINT_SetLowerLimit J,MinAng#/(180/Pi)
	TOKJOINT_SetUpperLimit J,MaxAng#/(180/Pi)
	TOKJOINT_EnableLimit J,e1
	TOKJOINT_SetLowerLimit2 J,MinAng2#/(180/Pi)
	TOKJOINT_SetUpperLimit2 J,MaxAng2#/(180/Pi)
	TOKJOINT_EnableLimit2 J,e2
End Function

;Hinge joint
Function opJointHingeCreate(x#=0,y#=0,z#=0,pitch#=0,yaw#=0,roll#=0,body2=0,body1=0,d#=0.5,e#=0.0,i#=10)	
	J = TOKJOINT_Create(3,body2,body1)
	TOKJOINT_SetPositionAndRotationWorld(J,x,y,z,pitch,yaw,roll)
	TOKJOINT_SetType(J,3)
	TOKJOINT_Enable(J,True)
	TOKJOINT_SetDampingFactor J,d# 
	TOKJOINT_SetEpsilon J,e#
	TOKJOINT_SetIterations J,i# 
	opJointCount=opJointCount+1
	Return J
End Function

Function opJointHingeSetLimit(j,MinAng#=0,MaxAng#=0)
	If j=0 RuntimeError "Tokamak Warning: There Was no Hinge joint found!" 
	TOKJOINT_SetLowerLimit J,MinAng#/(180/Pi)
	TOKJOINT_SetUpperLimit J,MaxAng#/(180/Pi)
	TOKJOINT_EnableLimit J,1
End Function

;Slider joint
Function opJointSliderCreate(x#=0,y#=0,z#=0,pitch#=0,yaw#=0,roll#=0,body2=0,body1=0,d#=0.5,e#=0.0,i#=10)
	j = TOKJOINT_Create(3,body2,body1)
	TOKJOINT_SetType j,4
	TOKJOINT_SetPositionAndRotationWorld j,x#,y#,z#,pitch#,yaw#,roll# 
	TOKJOINT_Enable j,True
	TOKJOINT_SetDampingFactor J,d# 
	TOKJOINT_SetEpsilon J,e#
	TOKJOINT_SetIterations J,i# 
	opJointCount=opJointCount+1
	Return J
End Function

Function opJointSliderSetLimit(j,Min#=0,Max#=0)
	If j=0 RuntimeError "Tokamak Warning: There Was no Hinge joint found!" 
	TOKJOINT_SetLowerLimit J,Min#
	TOKJOINT_SetUpperLimit J,Max#
	TOKJOINT_EnableLimit J,1
End Function

;create a Static trimesh, for example: level/map
Function opCreateTrimesh(mesh)
 scount=CountSurfaces(mesh)
 For ind=1 To scount
  surface=GetSurface(mesh,ind)
  ttltris=ttltris+CountTriangles(surface)
  ttlvert=ttlvert+CountVertices(surface)
 Next
 vertices=CreateBank(16*ttlvert)
 triangles=CreateBank(24*ttltris)
 offsetv=0
 offsett=0
 For ind=1 To scount
  surface = GetSurface(mesh,ind)
  ctr=CountTriangles(surface)
  tric=tric+cvt
  cvt=CountVertices(surface)
  ;fill bank with vertices
  For v=0 To cvt-1
   PokeFloat vertices,offsetv,VertexX#(surface,v)
   PokeFloat vertices,offsetv+4,VertexY#(surface,v)
   PokeFloat vertices,offsetv+8,VertexZ#(surface,v)
   PokeFloat vertices,offsetv+12,0.0
   offsetv=offsetv+16
  Next
  ;fill bank with triangles
  For v=0 To ctr-1
   PokeInt triangles,offsett,tric+TriangleVertex(surface,v,0)
   PokeInt triangles,offsett+4,tric+TriangleVertex(surface,v,1)
   PokeInt triangles,offsett+8,tric+TriangleVertex(surface,v,2)
   PokeInt triangles,offsett+12,2	; Material ID
   PokeInt triangles,offsett+16,0
   PokeInt triangles,offsett+20,0
   offsett=offsett+24
  Next
 Next

 ;Hand over the terrain data to Tokamak
 TOKSIM_SetStaticMesh vertices,ttlvert,triangles,ttltris
 ; Now we can free the banks as Tokamak has copied all data
 FreeBank vertices
 FreeBank triangles

End Function

;create a material
Function opCreateMaterial(id%,Friction#=0.84,Restitution#=0.5)
	TOKSIM_SetMaterial id%,Friction#,Restitution#
	Return id%
End Function

;function to apply material
Function opSetMaterial(geom,id%)
	TOKGEOM_SetMaterialIndex geom,id%
End Function

;removes the physical body from the world
Function opBodyDestroy(body,geom)
	TOKRB_Free body
	TOKRB_RemoveGeometry body,geom
End Function

;create magnetism or explosion at a position, negative intensity=magnetism positive intensity=explosion
Function opCreateMagnet(x#, y#, z#, range# = 5, intensity# = -10)
For p.tok = Each tok
	If p\body Then
		ox# = EntityX(p\mesh) - x#
		oy# = EntityY(p\mesh) - y#
		oz# = EntityZ(p\mesh) - z#
		dis# = Sqr(ox# * ox# + oy# * oy# + oz# * oz#) + 1
		effect# = 100 * intensity# / Float(dis# * dis#) / p\mass#
		px# = ox# * effect#
		py# = oy# * effect#
		pz# = oz# * effect#
		If dis# < range# Then
			TOKRB_ApplyImpulse p\body, px#/4, py#/4, pz#/4
		EndIf
	EndIf
Next
End Function

;create a water plane
Function opSetWater(y#=0,density#=.02,lin_damping#=.009,ang_damping#=.008)
	For p.tok = Each tok
		body=p\body
		y#=10
		density#=.02
		lin_damping#=.009;.01
		ang_damping#=.008;.01
		If TOKRB_GetY(body)<Y# Then
			If TOKRB_IsGravityEnabled(body) Then TOKRB_GravityEnable body,0
			TOKRB_ApplyImpulse body,0,density#,0
			TOKRB_SetAngularDamping body,lin_damping#
			TOKRB_SetLinearDamping body,ang_damping#
		ElseIf TOKRB_IsGravityEnabled(body)=0 Then
			TOKRB_GravityEnable body,1
			TOKRB_SetAngularDamping body,.002
			TOKRB_SetLinearDamping body,.001
		EndIf
	Next
End Function
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
; simplifyed functions
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
; Sounds
Global Snd_Explosion[6]
For i = 1 To 6
	Snd_Explosion[i] = Load3DSound("Sound\Explosion" + i + ".wav")
Next

Type Bomb
	Field time
	Field model
	Field Bomb.tok
End Type
;shoot Bombs
Function opShootBomb(ent,strenght#=20)
	b.Bomb = New Bomb
	b\Bomb.tok = opBodyCreateCapsule(.5,1,1)
	piv = CreatePivot()
	RotateEntity piv, EntityPitch(ent), EntityYaw(ent), 0
	MoveEntity piv, 0, 0, 3
	opBodySetPos(b\Bomb\body,EntityX(ent) + EntityX(piv), EntityY(ent) + EntityY(piv), EntityZ(ent) + EntityZ(piv))
	opBodySetrot(b\Bomb\body,EntityPitch(ent), EntityYaw(ent), EntityRoll(ent))
	TOKRB_ApplyImpulse b\Bomb\body, EntityX(piv) * strenght/4, (EntityY(piv) * strenght+strenght+strenght)/4, EntityZ(piv) * strenght/4
	;TOKRB_SetTorque b\Bomb\body,Rand(-2, 2), Rand(-2, 2), Rand(-2, 2)
	opBodySetGfx(b\Bomb\mesh,checktex,50,50,50)
	TOKRB_ApplyTwist b\Bomb\body,2,0,2
	
	FreeEntity piv
End Function
;update Bombs	
Function opUpdateBombs(listner)	
For b.Bomb = Each Bomb
	b\time = b\time + 1
	life=80
	If b\time = life Then
		range#=50
		intensity#=10
		b\model = CreateSphere()
		PositionEntity b\model, EntityX(b\Bomb\mesh), EntityY(b\Bomb\mesh), EntityZ(b\Bomb\mesh)
		EntityColor b\model,150,0,0		
		opCreateMagnet(EntityX(b\Bomb\mesh), EntityY(b\Bomb\mesh), EntityZ(b\Bomb\mesh), range#, intensity#)		
		EntityFX b\model, 1
		dis# = EntityDistance(b\Bomb\mesh, listner) * .3
		If dis# < 3 Then num = 1
		If dis# => 3 And dis# < 4.5 Then num = 2
		If dis# => 4.5 And dis# < 6.5 Then num = 3
		If dis# => 6.5 And dis# < 9 Then num = 4
		If dis# => 9 And dis# < 13.5 Then num = 5
		If dis# => 13.5 Then num = 6
		EmitSound Snd_Explosion[num], b\Bomb\mesh
		;Kill the object
		opBodyDestroy(b\Bomb\body,b\Bomb\geom)
		FreeEntity b\Bomb\mesh
		Delete b\Bomb
	ElseIf b\time > life Then
		ScaleMesh b\model, 1.15, 1.15, 1.15
		EntityAlpha b\model, 1 - Float(b\time - life) * .05
		If b\time > life*2 Then
			FreeEntity b\model
			Delete b
		EndIf
	EndIf
Next
End Function

;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
; interface functions
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------

;aply some visuals
Function opBodySetGfx(mesh,tex,r#=255,g#=255,b#=255,s#=.8)
			EntityColor mesh, r,g,b
			EntityTexture mesh,tex	
			EntityShininess mesh,s 
			;collision and pick
			EntityType mesh,COLL_PHYS
			EntityPickMode mesh,2
End Function

; create Checker texture
Function CreateCheckerTex()
	grid=CreateTexture( 64,64,1+8 )
	;ScaleTexture grid,.5,.5
	SetBuffer TextureBuffer( grid )
	Color 255,255,255:Rect 0,0,32,32
	Color 128,128,128:Rect 32,0,32,32
	Color 128,128,128:Rect 0,32,32,32
	Color 255,255,255:Rect 32,32,32,32
	Color 0,0,255
	SetBuffer BackBuffer()
	Color 255,255,255
	Return grid
End Function

; create Capsule mesh
Function CreateCapsule(segments=8,radius#=1,height#=1,parent=0)
	m = CreateCylinder(segments*2, parent)
	;radius=radius*.5
	height=height*.5
	ScaleMesh m,radius,height,radius
	RotateMesh m,0,0,0
		
	m1 = CreateSphere(segments)
	ScaleMesh m1,radius,radius,radius
	PositionMesh m1,0,-height,0
	AddMesh m1,m
	FreeEntity m1
	
	m1 = CreateSphere(segments)
	ScaleMesh m1,radius,radius,radius
	PositionMesh m1,0,height,0
	AddMesh m1,m
	FreeEntity m1
	Return m
End Function

; Free look the camera
Global FreeLookXS#, FreeLookZS#, FreeLookRotXS#, FreeLookRotYS#
Function FreeLook(camera, sp# = .1)
	If KeyDown(42) Then sp=sp*10
	If MouseDown(1)=1 Then opPushBody(camera)
	If sp# > 0 Then
		FreeLookXS# = (FreeLookXS# + ((KeyDown(32)) - (KeyDown(30))) * sp#) * .75
		FreeLookZS# = (FreeLookZS# + ((KeyDown(17) Or (MouseZSpeed()*10)) - (KeyDown(31) Or (MouseZSpeed()*10))) * sp#) * .75
		MoveEntity camera, FreeLookXS#, 0, FreeLookZS#
	EndIf
	FreeLookRotXS# = ((MouseXSpeed() - FreeLookRotXS#) * .2 + FreeLookRotXS#) * .9
	FreeLookRotYS# = ((MouseYSpeed() - FreeLookRotYS#) * .2 + FreeLookRotYS#) * .9
	If EntityPitch(camera) + FreeLookRotYS# < -89 pitch# = -89 ElseIf EntityPitch(camera) + FreeLookRotYS# > 89 pitch# = 89 Else pitch# = EntityPitch(camera) + FreeLookRotYS#
	yaw# = -FreeLookRotXS# + EntityYaw(camera)
	RotateEntity camera, pitch#, yaw#, 0
	MoveMouse GraphicsWidth() / 2, GraphicsHeight() / 2
End Function

;Pick a entity to push
Global pictentity,pnx#,pny#,pnz#,pcx#,pcy#,pcz#
Function campick(cam)
	pictentity = 0
	midw = GraphicsWidth()/2
	midh =GraphicsHeight()/2
	CameraPick(cam,midw,midh)
	pictentity = PickedEntity() 
	If Pictentity<>0
		
		;EntityAlpha pictentity,.7	
		pnx# = PickedNX#()
		pny# = PickedNY#()
		pnz# = PickedNZ#()	
		pcx# = PickedX#()
		pcy# = PickedY#()
		pcz# = PickedZ#()

	
	EndIf
End Function

;push the picked entity
Function opPushBody(cam,strenght#=15)	
		campick(cam)
		For p.tok = Each tok
			If pictentity = p\mesh
				
				i = n				
				If MouseDown(1)				
					TOKRB_ApplyImpulse2 p\body,-pnx#*strenght#,-pny#*strenght#,-pnz#*strenght#,pcx#,pcy#,pcz# 
				Else
					TOKRB_ApplyImpulse2 p\body,-pnx#,-pny#,-pnz#,pcx#,pcy#,pcz# 				;TOKRB_ApplyImpulse rb(i),-pnx#,pny#,-pnz#
				EndIf
			EndIf
		Next		
End Function

Function opSetLogo(cam)			
logo	=	LoadSprite("opLogo.tga",2)
			EntityParent logo,cam
			ScaleSprite logo,1,.125
			PositionEntity logo,1,-1.35,2
			EntityFX logo,1
			EntityOrder logo,-1
			Return logo
End Function 

;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
; Ray Casting / Sensors (like linepick, but faster)
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------

;createa a ray beam for the selected body
Function opCreateRay(body,x#,y#,z#,dx#,dy#,dz#)
	If body=0 RuntimeError "TOKAMAK Warning: body not found!"
	Sensor = TOKRB_AddSensor(body,x#,y#,z#,dx#,dy#,dz#)
	Return Sensor
End Function

;reposition the ray
Function opRaySetPosAndDir(ray,x#,y#,z#,dx#,dy#,dz#)
	If ray=0 RuntimeError "TOKAMAK Warning: ray/sensor not found!"
	TOKSENSOR_SetLineSensor(ray,x#,y#,z#,dx#,dy#,dz#)
End Function

;get the ray casted position
Function opRayGetX#(ray)
	If ray=0 RuntimeError "TOKAMAK Warning: ray/sensor not found!"
	get#=TOKSENSOR_GetDetectContactPointX(ray)
	Return get
End Function

Function opRayGetY#(ray)
	If ray=0 RuntimeError "TOKAMAK Warning: ray/sensor not found!"
	get#=TOKSENSOR_GetDetectContactPointY(ray)
	Return get
End Function

Function opRayGetZ#(ray)
	If ray=0 RuntimeError "TOKAMAK Warning: ray/sensor not found!"
	get#=TOKSENSOR_GetDetectContactPointZ(ray)
	Return get
End Function

;get the ray casted vector
Function opRayGetNX#(ray)
	If ray=0 RuntimeError "TOKAMAK Warning: ray/sensor not found!"
	get#=TOKSENSOR_GetDetectNormalX(ray)
	Return get
End Function

Function opRayGetNY#(ray)
	If ray=0 RuntimeError "TOKAMAK Warning: ray/sensor not found!"
	get#=TOKSENSOR_GetDetectNormalY(ray)
	Return get
End Function

Function opRayGetNZ#(ray)
	If ray=0 RuntimeError "TOKAMAK Warning: ray/sensor not found!"
	get#=TOKSENSOR_GetDetectNormalZ(ray)
	Return get
End Function

;get the ray casted depth
Function opRayGetDepth#(ray)
	If ray=0 RuntimeError "TOKAMAK Warning: ray/sensor not found!"
	get#=TOKSENSOR_GetDetectDepth(ray)
	Return get
End Function

;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
; Collision functions
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------
;----------------------------------------------------------------


Function opGetCollisionDepth#(colbank,i)
		depth#=Sqr(opGetColRelVelX#(colbank,i)*opGetColRelVelX#(colbank,i)+opGetColRelVelY#(colbank,i)*opGetColRelVelY#(colbank,i)+opGetColRelVelZ#(colbank,i)*opGetColRelVelZ#(colbank,i))
		Return depth#/3		
End Function
;collision internal functions
Function opCollbodyA(bank,index)
Return PeekInt(bank,index*104)
End Function

Function opCollbodyB(bank,index)
Return PeekInt(bank,index*104+4)
End Function

Function opGetTypeA(bank,index)
Return PeekInt(bank,index*104+8)
End Function

Function opGetTypeB(bank,index)
Return PeekInt(bank,index*104+12)
End Function

Function opGetGeometryA(bank,index)
Return PeekInt(bank,index*104+16)
End Function

Function opGetGeometryB(bank,index)
Return PeekInt(bank,index*104+20)
End Function

Function opGetMaterialIdA(bank,index)
Return PeekInt(bank,index*104+24)
End Function

Function opGetMaterialIdB(bank,index)
Return PeekInt(bank,index*104+28)
End Function

Function opBodyContactPointAX#(bank,index)
Return PeekFloat(bank,index*104+32)
End Function

Function opBodyContactPointAY#(bank,index)
Return PeekFloat(bank,index*104+36)
End Function

Function opBodyContactPointAZ#(bank,index)
Return PeekFloat(bank,index*104+40)
End Function

Function opBodyContactPointBX#(bank,index)
Return PeekFloat(bank,index*104+44)
End Function

Function opBodyContactPointBY#(bank,index)
Return PeekFloat(bank,index*104+48)
End Function

Function opBodyContactPointBZ#(bank,index)
Return PeekFloat(bank,index*104+52)
End Function

;collision position
Function opGetCollisionAX#(bank,index)
Return PeekFloat(bank,index*104+56)
End Function

Function opGetCollisionAY#(bank,index)
Return PeekFloat(bank,index*104+60)
End Function

Function opGetCollisionAZ#(bank,index)
Return PeekFloat(bank,index*104+64)
End Function

Function opGetCollisionBX#(bank,index)
Return PeekFloat(bank,index*104+68)
End Function

Function opGetCollisionBY#(bank,index)
Return PeekFloat(bank,index*104+72)
End Function

Function opGetCollisionBZ#(bank,index)
Return PeekFloat(bank,index*104+76)
End Function
;collision Relative velocity's
Function opGetColRelVelX#(bank,index)
Return PeekFloat(bank,index*104+80)
End Function

Function opGetColRelVelY#(bank,index)
Return PeekFloat(bank,index*104+84)
End Function

Function opGetColRelVelZ#(bank,index)
Return PeekFloat(bank,index*104+88)
End Function

;collision vectors
Function opGetCollisionNX#(bank,index)
Return PeekFloat(bank,index*104+92)
End Function

Function opGetCollisionNY#(bank,index)
Return PeekFloat(bank,index*104+96)
End Function

Function opGetCollisionNZ#(bank,index)
Return PeekFloat(bank,index*104+100)
End Function

;Some handy functions from RepeatUntil
Function opGetCollisionIdA(bank, index)
If opGetTypeA(bank, index) = 0 Then Return -1
If opGetTypeA(bank, index) = 1 Then Return TOKRB_GetCollisionID(opCollBodyA(bank, index))
If opGetTypeA(bank, index) = 2 Then Return TOKAB_GetCollisionID(opCollBodyA(bank, index))
Return 0
End Function

Function opGetCollisionIdB(bank, index)
If opGetTypeB(bank, index) = 0 Then Return -1
If opGetTypeB(bank, index) = 1 Then Return TOKRB_GetCollisionID(opCollBodyB(bank, index))
If opGetTypeB(bank, index) = 2 Then Return TOKAB_GetCollisionID(opCollBodyB(bank, index))
Return 0
End Function