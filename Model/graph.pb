
R
default_data_placeholderPlaceholder*
dtype0*
shape:?????????
h
dense_1_dense_kernel
VariableV2*
shape
:*
shared_name *
dtype0*
	container 
b
dense_1_dense_bias
VariableV2*
shared_name *
dtype0*
	container *
shape:
:
ConstConst*
dtype0*
valueB"      
D
Const_1Const*%
valueB	"               *
dtype0	
g
StatelessTruncatedNormalStatelessTruncatedNormalConstConst_1*
T0*
Tseed0	*
dtype0
8
Const_2Const*
valueB 2?????0??*
dtype0
=
CastCastConst_2*

SrcT0*
Truncate( *

DstT0
I
Init_dense_1_dense_kernelMulStatelessTruncatedNormalCast*
T0
?
Assign_dense_1_dense_kernelAssigndense_1_dense_kernelInit_dense_1_dense_kernel*
use_locking(*
T0*
validate_shape(
5
Const_3Const*
valueB:*
dtype0
4
Const_4Const*
valueB
 *???=*
dtype0
L
Init_dense_1_dense_biasFillConst_3Const_4*
T0*

index_type0
?
Assign_dense_1_dense_biasAssigndense_1_dense_biasInit_dense_1_dense_bias*
use_locking(*
T0*
validate_shape(
6
PlaceholderPlaceholder*
dtype0*
shape:
7
numberOfLossesPlaceholder*
shape: *
dtype0
o
MatMulMatMuldefault_data_placeholderdense_1_dense_kernel*
T0*
transpose_a( *
transpose_b( 
/
AddAddMatMuldense_1_dense_bias*
T0
 
SigmoidSigmoidAdd*
T0
0
Activation_dense_1IdentitySigmoid*
T0
h
SoftmaxCrossEntropyWithLogitsSoftmaxCrossEntropyWithLogitsActivation_dense_1Placeholder*
T0
1
Const_5Const*
value	B : *
dtype0
k
default_training_lossMeanSoftmaxCrossEntropyWithLogitsConst_5*

Tidx0*
	keep_dims( *
T0
>
Gradients/OnesLikeOnesLikedefault_training_loss*
T0
P
Gradients/ShapeShapeSoftmaxCrossEntropyWithLogits*
T0*
out_type0
9
Gradients/ConstConst*
dtype0*
value	B : 
;
Gradients/Const_1Const*
value	B :*
dtype0
@
Gradients/SizeSizeGradients/Shape*
T0*
out_type0
6
Gradients/AddAddConst_5Gradients/Size*
T0
<
Gradients/ModModGradients/AddGradients/Size*
T0
X
Gradients/RangeRangeGradients/ConstGradients/SizeGradients/Const_1*

Tidx0
8
Gradients/OnesLike_1OnesLikeGradients/Mod*
T0
?
Gradients/DynamicStitchDynamicStitchGradients/RangeGradients/ModGradients/ShapeGradients/OnesLike_1*
T0*
N
;
Gradients/Const_2Const*
value	B :*
dtype0
Q
Gradients/MaximumMaximumGradients/DynamicStitchGradients/Const_2*
T0
A
Gradients/DivDivGradients/ShapeGradients/Maximum*
T0
`
Gradients/ReshapeReshapeGradients/OnesLikeGradients/DynamicStitch*
T0*
Tshape0
S
Gradients/TileTileGradients/ReshapeGradients/Div*

Tmultiples0*
T0
R
Gradients/Shape_1ShapeSoftmaxCrossEntropyWithLogits*
T0*
out_type0
J
Gradients/Shape_2Shapedefault_training_loss*
out_type0*
T0
;
Gradients/Const_3Const*
value	B : *
dtype0
b
Gradients/ProdProdGradients/Shape_2Gradients/Const_3*
T0*

Tidx0*
	keep_dims( 
d
Gradients/Prod_1ProdGradients/Shape_1Gradients/Const_3*
T0*

Tidx0*
	keep_dims( 
;
Gradients/Const_4Const*
dtype0*
value	B :
J
Gradients/Maximum_1MaximumGradients/ProdGradients/Const_4*
T0
F
Gradients/Div_1DivGradients/Prod_1Gradients/Maximum_1*
T0
O
Gradients/CastCastGradients/Div_1*

SrcT0*
Truncate( *

DstT0
?
Gradients/Div_2DivGradients/TileGradients/Cast*
T0
J
Gradients/ZerosLike	ZerosLikeSoftmaxCrossEntropyWithLogits:1*
T0
J
Gradients/Const_5/ConstConst*
valueB :
?????????*
dtype0
a
Gradients/ExpandDims
ExpandDimsGradients/Div_2Gradients/Const_5/Const*

Tdim0*
T0
Y
Gradients/MultiplyMulGradients/ExpandDimsSoftmaxCrossEntropyWithLogits:1*
T0
?
Gradients/LogSoftmax
LogSoftmaxActivation_dense_1*
T0
D
Gradients/Const_6/ConstConst*
valueB
 *  ??*
dtype0
S
Gradients/Multiply_1MulGradients/LogSoftmaxGradients/Const_6/Const*
T0
J
Gradients/Const_7/ConstConst*
valueB :
?????????*
dtype0
c
Gradients/ExpandDims_1
ExpandDimsGradients/Div_2Gradients/Const_7/Const*

Tdim0*
T0
R
Gradients/Multiply_2MulGradients/ExpandDims_1Gradients/Multiply_1*
T0
;
Gradients/IdentityIdentityGradients/Multiply*
T0
_
Gradients/SigmoidGradSigmoidGradSigmoidGradients/Identity^Gradients/Identity*
T0
@
Gradients/Identity_1IdentityGradients/SigmoidGrad*
T0
@
Gradients/Identity_2IdentityGradients/SigmoidGrad*
T0
;
Gradients/Shape_3ShapeMatMul*
T0*
out_type0
G
Gradients/Shape_4Shapedense_1_dense_bias*
out_type0*
T0
g
Gradients/BroadcastGradientArgsBroadcastGradientArgsGradients/Shape_3Gradients/Shape_4*
T0
q
Gradients/SumSumGradients/Identity_1Gradients/BroadcastGradientArgs*

Tidx0*
	keep_dims( *
T0
W
Gradients/Reshape_1ReshapeGradients/SumGradients/Shape_3*
T0*
Tshape0
u
Gradients/Sum_1SumGradients/Identity_2!Gradients/BroadcastGradientArgs:1*
T0*

Tidx0*
	keep_dims( 
Y
Gradients/Reshape_2ReshapeGradients/Sum_1Gradients/Shape_4*
T0*
Tshape0
t
Gradients/MatMulMatMulGradients/Reshape_1dense_1_dense_kernel*
transpose_a( *
transpose_b(*
T0
z
Gradients/MatMul_1MatMuldefault_data_placeholderGradients/Reshape_1*
transpose_b( *
T0*
transpose_a(
=
ShapeShapedense_1_dense_kernel*
T0*
out_type0
4
Const_6Const*
valueB
 *    *
dtype0
X
%Init_optimizer_dense_1_dense_kernel-mFillShapeConst_6*

index_type0*
T0
t
 optimizer_dense_1_dense_kernel-m
VariableV2*
dtype0*
	container *
shape
:*
shared_name 
?
'Assign_optimizer_dense_1_dense_kernel-mAssign optimizer_dense_1_dense_kernel-m%Init_optimizer_dense_1_dense_kernel-m*
validate_shape(*
use_locking(*
T0
?
Shape_1Shapedense_1_dense_kernel*
T0*
out_type0
4
Const_7Const*
valueB
 *    *
dtype0
Z
%Init_optimizer_dense_1_dense_kernel-vFillShape_1Const_7*
T0*

index_type0
t
 optimizer_dense_1_dense_kernel-v
VariableV2*
	container *
shape
:*
shared_name *
dtype0
?
'Assign_optimizer_dense_1_dense_kernel-vAssign optimizer_dense_1_dense_kernel-v%Init_optimizer_dense_1_dense_kernel-v*
use_locking(*
T0*
validate_shape(
=
Shape_2Shapedense_1_dense_bias*
T0*
out_type0
4
Const_8Const*
valueB
 *    *
dtype0
X
#Init_optimizer_dense_1_dense_bias-mFillShape_2Const_8*
T0*

index_type0
n
optimizer_dense_1_dense_bias-m
VariableV2*
	container *
shape:*
shared_name *
dtype0
?
%Assign_optimizer_dense_1_dense_bias-mAssignoptimizer_dense_1_dense_bias-m#Init_optimizer_dense_1_dense_bias-m*
use_locking(*
T0*
validate_shape(
=
Shape_3Shapedense_1_dense_bias*
T0*
out_type0
4
Const_9Const*
valueB
 *    *
dtype0
X
#Init_optimizer_dense_1_dense_bias-vFillShape_3Const_9*
T0*

index_type0
n
optimizer_dense_1_dense_bias-v
VariableV2*
shared_name *
dtype0*
	container *
shape:
?
%Assign_optimizer_dense_1_dense_bias-vAssignoptimizer_dense_1_dense_bias-v#Init_optimizer_dense_1_dense_bias-v*
validate_shape(*
use_locking(*
T0
a
optimizer_beta1_power
VariableV2*
	container *
shape: *
shared_name *
dtype0
G
Init_optimizer_beta1_powerConst*
valueB
 *fff?*
dtype0
?
Assign_optimizer_beta1_powerAssignoptimizer_beta1_powerInit_optimizer_beta1_power*
validate_shape(*
use_locking(*
T0
a
optimizer_beta2_power
VariableV2*
shape: *
shared_name *
dtype0*
	container 
G
Init_optimizer_beta2_powerConst*
valueB
 *w??*
dtype0
?
Assign_optimizer_beta2_powerAssignoptimizer_beta2_powerInit_optimizer_beta2_power*
use_locking(*
T0*
validate_shape(
5
Const_10Const*
dtype0*
valueB
 *fff?
5
Const_11Const*
valueB
 *w??*
dtype0
5
Const_12Const*
valueB
 *o?:*
dtype0
5
Const_13Const*
valueB
 *???3*
dtype0
?
	ApplyAdam	ApplyAdamdense_1_dense_kernel optimizer_dense_1_dense_kernel-m optimizer_dense_1_dense_kernel-voptimizer_beta1_poweroptimizer_beta2_powerConst_12Const_10Const_11Const_13Gradients/MatMul_1*
T0*
use_nesterov( *
use_locking( 
?
ApplyAdam_1	ApplyAdamdense_1_dense_biasoptimizer_dense_1_dense_bias-moptimizer_dense_1_dense_bias-voptimizer_beta1_poweroptimizer_beta2_powerConst_12Const_10Const_11Const_13Gradients/Reshape_2*
use_nesterov( *
use_locking( *
T0
4
MulMuloptimizer_beta1_powerConst_10*
T0
^
AssignAssignoptimizer_beta1_powerMul*
T0*
validate_shape(*
use_locking(
6
Mul_1Muloptimizer_beta2_powerConst_11*
T0
b
Assign_1Assignoptimizer_beta2_powerMul_1*
validate_shape(*
use_locking(*
T0
6
default_outputSoftmaxActivation_dense_1*
T0 "?