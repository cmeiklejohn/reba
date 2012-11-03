# reba

Prototype of a binding library authored in ClojureScript.  Reba is just an experiment and shouldn't be used in production.

## Usage

Currently, the reba consists of two main protocols: ```materializable```
and ```eventable```.

## Materializers

Materializers provide a way to bind a Clojure atom to a particular location in the DOM and keep the DOM updated with changes to the object as they occur.

The following code will bind a materializer named ```completed-list-view``` to the Clojure atom ```list-of-items```, and render the output of the some-html-generating-fn (when applied to the current value of the atom) into the element in the DOM identified by ```completed```.

```clojure
(materializable/add!
	list-of-items
	:completed-list-view
	"completed"
  some-html-generating-fn)
```

## Eventables

Eventables provide a way to bind event listeners to particular locations in the DOM, and tie them to a particular Clojure atom for mutating state.  Combined with Materializers, Eventables provide the ability to have an event triggered from the DOM, update a Clojure atom, and propagate the result of the action directly back into the DOM.

In the following example, a listener is bound to the element in the DOM identified by ```add-todo`` for the ```click``` event.  When this event is received, the ```add-event-handler``` function will be applied to the ```list-of-items``` atom with the actual event, and its return value will be used to replace the current contents of ```list-of-items```.  Combined with Materializers, this can be used to propagate the changes directly back into the DOM.

```clojure
(eventable/add!
	list-of-items
	"add-todo"
	"click"
	add-event-handler))
```

## Examples

See the ```src/examples``` directory for examples of more full-featured web applications.

## License

Copyright (C) 2012 Christopher Meiklejohn.

Distributed under the Eclipse Public License, the same as Clojure.
