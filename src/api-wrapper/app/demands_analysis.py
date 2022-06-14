from fastapi_camelcase import CamelModel
from pydantic import Field
from typing import List, Optional


class Resource(CamelModel):
    """
    Resource needed to satisfy a demand
    """

    class_of_supply: int = Field(
        ..., description="Class of supply associated with this resource"
    )
    name: str = Field(..., description="Name of this resource")
    unit_mass: float = Field(..., description="Mass (kg) of 1.0 units of this resource")
    unit_volume: float = Field(
        ..., description="Volume (m^3) of 1.0 units of this resource"
    )


class Location(CamelModel):
    """
    Spatial location in a scenario
    """

    id: int = Field(..., description="ID of this location")
    name: str = Field(..., description="Name of this location")


class Element(CamelModel):
    """
    Persistent element taking part in a scenario
    """

    id: int = Field(..., description="ID of this element")
    name: str = Field(..., description="Name of this element")


class Demand(CamelModel):
    """
    Demand for an amount of a resource
    """

    resource: Resource = Field(..., description="Type of resource demanded")
    amount: float = Field(..., description="Amount of resource demanded (units)")
    mass: float = Field(..., description="Mass (kg) of this demand")
    volume: float = Field(..., description="Volume (m^3) of this demand")


class RawDemand(CamelModel):
    """
    Set of demands aggregated to a moment in time
    """

    time: float = Field(
        ..., description="Time (days relative to scenario start) of this demand"
    )
    location: Location = Field(
        ..., description="Location (node) associated with this demand"
    )
    element: Optional[Element] = Field(
        None, description="Element associated with this demand"
    )
    demands: List[Demand] = Field(
        [], description="List of demands aggregated to this moment"
    )
    total_mass: float = Field(
        ..., description="Total mass (kg) of all demands aggregated to this moment"
    )
    total_volume: float = Field(
        ..., description="Total volume (m^3) of all demands aggregated to this moment"
    )


class RawDemandsAnalysis(CamelModel):
    """
    Set of demands aggregated to a moment in time
    """

    demands: List[RawDemand] = Field(
        [], description="List of demands aggregated to moments in time"
    )


class NodeDemand(CamelModel):
    """
    Demands aggregated to a supply node
    """

    time: float = Field(
        ...,
        description="Time (days relative to scenario start) of a supply opportunity to a node",
    )
    location: Location = Field(
        ..., description="Location associated with this supply node"
    )
    demands: List[Demand] = Field(
        [], description="List of demands aggregated to this supply node"
    )
    total_mass: float = Field(
        ..., description="Total mass (kg) of all demands aggregated to this supply node"
    )
    total_volume: float = Field(
        ...,
        description="Total volume (m^3) of all demands aggregated to this supply node",
    )


class EdgeDemand(CamelModel):
    """
    Demands aggregated to a supply edge
    """

    start_time: float = Field(
        ..., description="Start time (days relative to scenario start)"
    )
    end_time: float = Field(
        ..., description="End time (days relative to scenario start)"
    )
    origin: Location = Field(..., description="Origin of this edge")
    destination: Location = Field(..., description="Destination of this edge")
    location: Location = Field(
        ..., description="Location (edge) associated with this supply edge"
    )
    demands: List[Demand] = Field(
        [], description="List of demands aggregated to this supply edge"
    )
    total_mass: float = Field(
        ..., description="Total mass (kg) of all demands aggregated to this supply edge"
    )
    total_volume: float = Field(
        ...,
        description="Total volume (m^3) of all demands aggregated to this supply edge",
    )
    max_cargo_mass: float = Field(
        ...,
        description="Max cargo mass (kg) available on all carriers in this supply edge",
    )
    net_cargo_mass: float = Field(
        ...,
        description="Net cargo mass (kg) remaining on all carriers in this supply edge",
    )
    max_cargo_volume: float = Field(
        ...,
        description="Max cargo volume (m^3) available on all carriers in this supply edge",
    )
    net_cargo_volume: float = Field(
        ...,
        description="Net cargo volume (m^3) remaining on all carriers in this supply edge",
    )


class AggregatedDemandsAnalysis(CamelModel):
    """
    List of demands aggregated to supply nodes and edges
    """

    nodes: List[NodeDemand] = Field(
        [], description="List of demands aggregated to supply nodes"
    )
    edges: List[EdgeDemand] = Field(
        [], description="List of demands aggregated to supply edges"
    )
