.lib " "

opWorldCreate(plane%)
opWorldStep(dtime#,sub%)
opDestroyWorld()

opBodySetEnt(body,ent)
opBodySetBox(mesh,mass#)
opBodySetSphere(mesh,mass#)
opBodySetCapsule(mesh,mass#)

opBodyCreateBox(w#,h#,d#,mass#)
opBodyCreateSphere(radius#,mass#)
opBodyCreateCapsule(radius#, height#,mass#)
opBodyCreateCompound()

opBodyAddCompoundBox(body,mesh,w#,h#,d#,x#,y#,z#,pitch#,yaw#,roll#)
opBodyAddCompoundSphere(body,mesh,radius#,x#,y#,z#)	
opBodyAddCompoundCapsule(body,mesh,radius#,height#,x#,y#,z#,pitch#,yaw#,roll#)	
opBodySetCompound(body,mesh,mass#,Debug)

opLoadConvex(file$)
opBodyGenerateConvex(meshobj,file$)
opBodyCreateHull(meshobj,Convex,mass#)
opBodyMakeHull(meshobj,file$,mass#)

opBodySetPos(body,x#,y#,z#)
opBodySetCOM(body,geom,mesh,CoMx#,CoMy#,CoMz#)
opBodySetRot(body,x#,y#,z#)
opBodySetVel(body,x#,y#,z#)

opBodyGetSleep(body)
opBodySetAutoSleepTreshold(body,sleepparam#)

opJointBallCreate(x#,y#,z#,pitch#,yaw#,roll#,body2,body1,d#,e#,i#)	
opJointBallSetLimit(j,e1,MinAng#,MaxAng#,e2,MinAng2#,MaxAng2#)
opJointHingeCreate(x#,y#,z#,pitch#,yaw#,roll#,body2,body1,d#,e#,i#)	
opJointHingeSetLimit(j,MinAng#,MaxAng#)
opJointSliderCreate(x#,y#,z#,pitch#,yaw#,roll#,body2,body1,d#,e#,i#)
opJointSliderSetLimit(j,Min#,Max#)

opCreateTrimesh(mesh)
opCreateMaterial(id%,Friction#,Restitution#)
opSetMaterial(geom,id%)
opBodyDestroy(body,geom)

opCreateMagnet(x#, y#, z#, range#, intensity#)
opSetWater(y#,density#,lin_damping#,ang_damping#)