# Coordinated Throughput Controller

<span class=''>[<i class='fa fa-download'></i> Download](/?search=coordinated-throughput-controller)</span>

Coordinated Throughput Controller is a JMeter Logic Controller for weighted,
mutually-exclusive branch selection.

JMeter's standard Throughput Controller evaluates each sibling independently.
If two sibling Throughput Controllers are both configured at 50%, both can run
during the same parent iteration. Coordinated Throughput Controller makes one
shared decision across sibling coordinated controllers under the same immediate
parent, so only one coordinated branch runs per parent iteration.

## Basic Usage

Place two or more Coordinated Throughput Controllers as siblings under the same
parent controller, then place each branch's samplers/controllers under the
corresponding coordinated controller.

Example:

```text
Thread Group
  Loop Controller
    Coordinated Throughput Controller - Login Flow
      HTTP Request - Login
      HTTP Request - Fetch Profile
    Coordinated Throughput Controller - Browse Flow
      HTTP Request - Browse Products
      HTTP Request - Product Details
```

If the Login Flow has weight `30` and the Browse Flow has weight `40`, then
across parent iterations:

```text
Login Flow  -> about 30%
Browse Flow -> about 40%
No branch   -> about 30%
```

## Weight Behavior

Weights are deterministic, not random.

If sibling weights total `100`, exactly one coordinated branch runs each parent
iteration. If weights total less than `100`, the remaining percentage means
"run none of the coordinated branches." If weights total more than `100`, the
weights act as relative weights and there is no no-selection remainder.
